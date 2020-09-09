package com.demo.thread;



import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 异步任务的代理类
 *
 * @author 优化取自xUitls的线程池工具类
 */
public class TaskProxy<ResultType> extends AbsTask<ResultType> {

    static final InternalHandler sHandler = new InternalHandler();
    static final PriorityExecutor sDefaultExecutor = new PriorityExecutor(true);
    private final WeakReference<Activity> weakActivity;

    private final AbsTask<ResultType> task;
    private final Executor executor;
    private volatile boolean callOnCanceled = false;
    private volatile boolean callOnFinished = false;

    /**
     * TODO 不考虑内存泄漏
     *
     * @param task task
     */
    TaskProxy(AbsTask task) {
        super(task);
        this.task = task;
        this.task.setTaskProxy(this);
        this.setTaskProxy(null);
        Executor taskExecutor = task.getExecutor();
        if (taskExecutor == null) {
            taskExecutor = sDefaultExecutor;
        }
        this.executor = taskExecutor;
        this.weakActivity = null;
    }

    /**
     * TODO 考虑内存泄漏
     *
     * @param activity activity
     * @param task     task
     */
    TaskProxy(Activity activity, AbsTask task) {
        super(task);
        this.task = task;
        this.task.setTaskProxy(this);
        this.setTaskProxy(null);
        Executor taskExecutor = task.getExecutor();
        if (taskExecutor == null) {
            taskExecutor = sDefaultExecutor;
        }
        this.executor = taskExecutor;
        this.weakActivity = new WeakReference<>(activity);
    }

    @Override
    protected final ResultType doBackground() throws Throwable {
        this.onWaiting();
        PriorityRunnable runnable = new PriorityRunnable(task.getPriority(),
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //等待过程中取消
                            if (callOnCanceled || TaskProxy.this.isCancelled()) {
                                throw new Callback.CancelledException("");
                            }
                            //start running
                            TaskProxy.this.onStart();

                            //开始时取消
                            if (TaskProxy.this.isCancelled()) {
                                throw new Callback.CancelledException("");
                            }

                            //执行task，得到结果
                            task.setResult(task.doBackground());
                            TaskProxy.this.setResult(task.getResult());

                            //未在doBackground过程中取消成功
                            if (TaskProxy.this.isCancelled()) {
                                throw new Callback.CancelledException("");
                            }

                            if (activityIsNoFinishing()) {// 避免内存泄漏
                                //执行成功
                                TaskProxy.this.onSuccess(task.getResult());
                            }
                        } catch (Callback.CancelledException cex) {
                            if (activityIsNoFinishing()) {// 避免内存泄漏
                                TaskProxy.this.onCancelled(cex);
                            }
                        } catch (Throwable ex) {
                            if (activityIsNoFinishing()) {// 避免内存泄漏
                                TaskProxy.this.onError(ex, false);
                            }
                        } finally {
                            if (activityIsNoFinishing()) {// 避免内存泄漏
                                TaskProxy.this.onFinished();
                            }
                        }
                    }
                });
        this.executor.execute(runnable);
        return null;
    }

    /**
     * 判断Activity是否finished，避免内存泄漏
     *
     * @return 未finished
     */
    private boolean activityIsNoFinishing() {
        return weakActivity == null || (weakActivity.get() != null && !weakActivity.get().isFinishing() && !weakActivity.get().isDestroyed());
    }

    @Override
    protected void onWaiting() {
        this.setState(State.WAITING);
        sHandler.obtainMessage(MSG_WHAT_ON_WAITING, this)
                .sendToTarget();
    }

    @Override
    protected void onStart() {
        this.setState(State.STARTED);
        sHandler.obtainMessage(MSG_WHAT_ON_START, this)
                .sendToTarget();
    }

    @Override
    protected void onSuccess(Object result) {
        this.setState(State.SUCCESS);
        sHandler.obtainMessage(MSG_WHAT_ON_SUCCESS, this)
                .sendToTarget();
    }

    @Override
    protected void onError(Throwable ex, boolean isCallbackError) {
        this.setState(State.CANCELLED);
        sHandler.obtainMessage(MSG_WHAT_ON_ERROR, new ArgsObj(this, ex))
                .sendToTarget();
    }

    @Override
    protected void onUpdate(int flag, Object... args) {
        sHandler.obtainMessage(MSG_WHAT_ON_UPDATE, flag, flag, new ArgsObj(this, args))
                .sendToTarget();
    }

    @Override
    protected void onCancelled(Callback.CancelledException cex) {
        this.setState(State.CANCELLED);
        sHandler.obtainMessage(MSG_WHAT_ON_CANCEL, new ArgsObj(this, cex))
                .sendToTarget();
    }

    @Override
    protected void onFinished() {
        sHandler.obtainMessage(MSG_WHAT_ON_FINISHED, this)
                .sendToTarget();
    }

    @Override
    void setState(State state) {
        super.setState(state);
        this.task.setState(state);
    }

    @Override
    public Priority getPriority() {
        return task.getPriority();
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    private static class ArgsObj {
        final TaskProxy taskProxy;
        final Object[] args;

        public ArgsObj(TaskProxy taskProxy, Object... args) {
            this.taskProxy = taskProxy;
            this.args = args;
        }
    }

    private final static int MSG_WHAT_BASE = 1000000000;
    private final static int MSG_WHAT_ON_WAITING = MSG_WHAT_BASE + 1;
    private final static int MSG_WHAT_ON_START = MSG_WHAT_BASE + 2;
    private final static int MSG_WHAT_ON_SUCCESS = MSG_WHAT_BASE + 3;
    private final static int MSG_WHAT_ON_ERROR = MSG_WHAT_BASE + 4;
    private final static int MSG_WHAT_ON_UPDATE = MSG_WHAT_BASE + 5;
    private final static int MSG_WHAT_ON_CANCEL = MSG_WHAT_BASE + 6;
    private final static int MSG_WHAT_ON_FINISHED = MSG_WHAT_BASE + 7;

    final static class InternalHandler extends Handler {

        private InternalHandler() {
            super(Looper.getMainLooper());
        }


        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                throw new IllegalArgumentException("msg must not be null");
            }
            TaskProxy taskProxy = null;
            Object[] args = null;
            if (msg.obj instanceof TaskProxy) {
                taskProxy = (TaskProxy) msg.obj;
            } else if (msg.obj instanceof ArgsObj) {
                ArgsObj argsObj = (ArgsObj) msg.obj;
                taskProxy = argsObj.taskProxy;
                args = argsObj.args;
            }
            if (taskProxy == null) {
                throw new RuntimeException("msg.obj not instanceof TaskProxy");
            }

            try {
                switch (msg.what) {
                    case MSG_WHAT_ON_WAITING:
                        taskProxy.task.onWaiting();
                        break;
                    case MSG_WHAT_ON_START:
                        taskProxy.task.onStart();
                        break;
                    case MSG_WHAT_ON_SUCCESS:
                        taskProxy.task.onSuccess(taskProxy.getResult());
                        break;
                    case MSG_WHAT_ON_ERROR:
                        assert args != null;
                        Throwable throwable = (Throwable) args[0];
                        taskProxy.task.onError(throwable, false);
                        break;
                    case MSG_WHAT_ON_UPDATE:
                        taskProxy.task.onUpdate(msg.arg1, args);
                        break;
                    case MSG_WHAT_ON_CANCEL:
                        if (taskProxy.callOnCanceled) {
                            return;
                        }
                        taskProxy.callOnCanceled = true;
                        assert args != null;
                        taskProxy.task.onCancelled((com.demo.thread.Callback.CancelledException) args[0]);
                        break;
                    case MSG_WHAT_ON_FINISHED:
                        if (taskProxy.callOnFinished) {
                            return;
                        }
                        taskProxy.callOnFinished = true;
                        taskProxy.task.onFinished();
                        break;
                    default:
                        break;
                }
            } catch (Throwable ex) {
                taskProxy.setState(State.ERROR);
                if (msg.what != MSG_WHAT_ON_ERROR) {
                    taskProxy.task.onError(ex, true);
                }
            }
            super.handleMessage(msg);
        }
    }
}