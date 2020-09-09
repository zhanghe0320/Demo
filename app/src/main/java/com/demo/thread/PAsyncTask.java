package com.demo.thread;



import android.app.Activity;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PAAsyncTask
 * TODO 主要用到的是execute()方法，也提供submit()方法，类似AsyncTask的start()方法等。
 *
 * @author 优化取自xUitls的线程池工具类
 */
public class PAsyncTask implements TaskController {
//PAsyncTask.getInstance().execute(new Runnable() {
//        @Override
//        public void run() {
//            // 处理耗时操作
//        }
//    });
//    private Future<?> readerThread;
//    readerThread = PAsyncTask.getInstance().submit(new Runnable() {
//        @Override
//        public void run() {
//            // 处理耗时操作
//        }
//    });
//        readerThread.cancel(true);
//PAsyncTask.getInstance().start(activity，new AbsTask<List<GroupEntity> >() {
//        @Override
//        protected List<GroupEntity> doBackground() throws Throwable {
//
//        }
//
//        @Override
//        protected void onSuccess(Object result) {
//            // 回到主线程操作，start后面传参activity为考试内存泄漏，不传则不考虑。
//            List<GroupEntity> res = (List<GroupEntity>) result;
//        }
//
//        @Override
//        protected void onError(Throwable ex, boolean isCallbackError) {
//            // 错误回调
//        }
//    });
    private PAsyncTask() {
    }

    // 可见性，不具有原子性
    private static volatile TaskController instance;

    public static TaskController getInstance() {
        if (instance == null) {
            synchronized (TaskController.class) {
                if (instance == null) {
                    instance = new PAsyncTask();
                }
            }
        }
        return instance;
    }

    @Override
    public void autoPost(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            TaskProxy.sHandler.post(runnable);
        }
    }

    @Override
    public void post(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        TaskProxy.sHandler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillis) {
        if (runnable == null) {
            return;
        }
        TaskProxy.sHandler.postDelayed(runnable, delayMillis);
    }

    @Override
    public void execute(Runnable runnable) {
        TaskProxy.sDefaultExecutor.execute(runnable);
    }

    @Override
    public void executeNewCached(Runnable runnable) {
        Executors.newCachedThreadPool().execute(runnable);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return TaskProxy.sDefaultExecutor.submit(runnable);
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        TaskProxy.sHandler.removeCallbacks(runnable);
    }

    @Override
    public void removeCallbacks(Runnable... runnable) {
        if (runnable != null && runnable.length > 0) {
            for (Runnable r : runnable) {
                removeCallbacks(r);
            }
        }
    }

    @Override
    public <T> AbsTask<T> start(AbsTask<T> task) {
        TaskProxy<T> proxy;
        if (task instanceof TaskProxy) {
            proxy = (TaskProxy<T>) task;
        } else {
            proxy = new TaskProxy<>(task);
        }
        try {
            proxy.doBackground();
        } catch (Throwable ex) {
            Log.e("PAAsyncTask", ex.getMessage());
        }
        return proxy;
    }

    @Override
    public <T> AbsTask<T> start(@NonNull Activity activity, AbsTask<T> task) {
        TaskProxy<T> proxy;
        if (task instanceof TaskProxy) {
            proxy = (TaskProxy<T>) task;
        } else {
            proxy = new TaskProxy<>(activity, task);
        }
        try {
            proxy.doBackground();
        } catch (Throwable ex) {
            Log.e("PAAsyncTask", ex.getMessage());
        }
        return proxy;
    }

    @Override
    public <T> T startSync(AbsTask<T> task) throws Throwable {
        T result = null;
        try {
            task.onWaiting();
            task.onStart();
            result = task.doBackground();
            task.onSuccess(result);
        } catch (Callback.CancelledException cex) {
            task.onCancelled(cex);
        } catch (Throwable ex) {
            task.onError(ex, false);
        } finally {
            task.onFinished();
        }
        return result;
    }

    @Override
    public <T extends AbsTask<?>> Callback.Cancelable startTasks(final Callback.GroupCallback<T> groupCallback, final T... tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        final Runnable callIfOnAllFinished = new Runnable() {

            private final int total = tasks.length;
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public void run() {
                if (count.incrementAndGet() == total) {
                    if (groupCallback != null) {
                        groupCallback.onAllFinished();
                    }
                }
            }
        };
        for (final T task : tasks) {
            start(new TaskProxy<T>(task) {
                @Override
                protected void onSuccess(Object result) {
                    super.onSuccess(result);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (groupCallback != null) {
                                groupCallback.onSuccess(task);
                            }
                        }
                    });
                }

                @Override
                protected void onCancelled(final Callback.CancelledException cex) {
                    super.onCancelled(cex);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (groupCallback != null) {
                                groupCallback.onCancelled(task, cex);
                            }
                        }
                    });
                }

                @Override
                protected void onError(final Throwable ex, final boolean isCallbackError) {
                    super.onError(ex, isCallbackError);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (groupCallback != null) {
                                groupCallback.onError(task, ex, isCallbackError);
                            }
                        }
                    });
                }

                @Override
                protected void onFinished() {
                    super.onFinished();
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (groupCallback != null) {
                                groupCallback.onFinished(task);
                            }
                            callIfOnAllFinished.run();
                        }
                    });
                }
            });
        }
        return new Callback.Cancelable() {
            @Override
            public void cancel() {
                for (T task : tasks) {
                    task.cancel();
                }
            }

            @Override
            public boolean isCancelled() {
                boolean isCancelled = true;
                for (T task : tasks) {
                    if (!task.isCancelled()) {
                        isCancelled = false;
                    }
                }
                return isCancelled;
            }
        };
    }

    @Override
    public ExecutorService udfThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, String threadName, boolean threadDaemon) {
        return UdfThreadPoolUtils.udfThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, threadName, threadDaemon);
    }

    @Override
    public ExecutorService udfNewSingleThreadExecutor(String threadName, boolean threadDaemon) {
        return UdfThreadPoolUtils.udfNewSingleThreadExecutor(threadName, threadDaemon);
    }

    @Override
    public ScheduledExecutorService udfNewSingleThreadScheduledExecutor(String threadName, boolean threadDaemon) {
        return UdfThreadPoolUtils.udfNewSingleThreadScheduledExecutor(threadName, threadDaemon);
    }
}

