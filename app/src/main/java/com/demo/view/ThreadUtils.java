package com.demo.view;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class ThreadUtils {
    
    /**
     * 全局线程池，支持后台立即运行、后台延迟运行、UI线程执行
     * <p>
     * 考虑到浏览器中绝大部分的后台线程都是IO密集型，因此线程池的数量设置为2*N + 1，N为cpu数量
     * <p>
     * 线程池中线程的优先级为(Thread.MIN_PRIORITY + Thread.NORM_PRIORITY) / 2 + 1,该值为4，对应为Android中的
     * 标准线程后台优先级Process.THREAD_PRIORITY_BACKGROUND,并且该值可不修改
     * <p>
     * 不建议使用全局的HandlerThread来执行后台任务，如果在一个全局的HandlerThread中提交了大量的Runnable，这些Runnable会
     * 顺序执行，效率太低，建议Runnable放入本例中的线程池中，由于线程池中活动线程的是数目为2*N + 1， 会大大提高任务的排队效率
     * <p>
     * 该类提供了4种优先级级别的线程池（或HandlerThread）
     * 1. 在UI线程执行，优先级略高于Thread.NORM_PRIORITY：在此HandlerThread中执行更新UI的操作
     * 2. 在标准后台线程优先级的线程池中执行，优先级为(Thread.MIN_PRIORITY + Thread.NORM_PRIORITY) / 2 + 1
     * 3. 在优先级为Thread.NORM_PRIORITY的HandlerThread中执行：在此HandlerThread中执行较紧急的后台操作
     * 4. 在优先级为Thread.MIN_PRIORITY的HandlerThread中执行：在此HandlerThread中执行非常不紧急的后台操作
     */
    
    private static String TAG = ThreadUtils.class.getSimpleName();
    
    /**
     * 线程池中运行的线程数目
     */
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 1;
    
    /**
     * 线程优先级，该值对应为linux中的10，普通的后台线程使用该优先级
     */
    private static final int PRIORITY_STD_BACKGROUND = (Thread.MIN_PRIORITY + Thread.NORM_PRIORITY) / 2 + 1;
    
    /**
     * 线程优先级，该值对应为linux中的19，该优先级的线程将获取最少的执行事件，慎用！
     */
    private static final int PRIORITY_LOWEST_BACKGROUND = Thread.MIN_PRIORITY;
    
    /**
     * 线程优先级，该值对应为linux中的0，该优先级接近ui线程的优先级，慎用！
     */
    private static final int PRIORITY_URGENT_BACKGROUND = Thread.NORM_PRIORITY;
    
    /**
     * 单例对象
     */
    private static volatile ThreadUtils sInstance;
    
    /**
     * 线程池，提供普通任务、延时任务和循环任务3种模式
     */
    private ScheduledExecutorService mScheduledPool;
    
    /**
     * 用于在主线程执行任务
     */
    private Handler mMainHandler;
    
    /**
     * 需要后台执行、但又非常紧急的任务，可post到该handler
     * 注意：由于该线程的优先级较高，只有非常紧急的任务才可post到该handler中
     */
    private Handler mUrgentHandler;
    
    /**
     * 需要后台执行、但又非常不紧急、不影响用户操作的任务可post到该handler中
     * 注意：由于该线程的优先级为最低，任务执行缓慢，非特殊情况，请使用标准后台线程优先级的线程池来执行
     */
    private Handler mLowestHandler;
    
    private HandlerThread mUrgentHandlerThread;
    
    private HandlerThread mLowestHandlerThread;
    
    /**
     * DCL
     *
     * @return
     */
    public static ThreadUtils getInstance() {
        if (sInstance == null) { // NOPMD
            synchronized (ThreadUtils.class) {
                if (sInstance == null) {
                    sInstance = new ThreadUtils();
                }
            }
        }
        return sInstance;
    }
    
    /**
     * 私有构造函数
     */
    private ThreadUtils() {
        mMainHandler = new Handler(Looper.getMainLooper());
        mScheduledPool = Executors.newScheduledThreadPool(THREAD_COUNT, new ThreadUtils.NormalAsyncThreadFactory());
        
        mUrgentHandlerThread = new HandlerThread("urgent_thread_loop_handler");
        mUrgentHandlerThread.setPriority(PRIORITY_URGENT_BACKGROUND);
        mUrgentHandlerThread.start();
        mUrgentHandler = new Handler(mUrgentHandlerThread.getLooper());
        
        mLowestHandlerThread = new HandlerThread("lowest_thread_loop_handler");
        mLowestHandlerThread.setPriority(PRIORITY_LOWEST_BACKGROUND);
        mLowestHandlerThread.start();
        mLowestHandler = new Handler(mLowestHandlerThread.getLooper());
    }
    
    /**
     * 获取线程池
     *
     * @return
     */
    public ExecutorService getExecutor() {
        return mScheduledPool;
    }
    
    /**
     * 在Ui线程执行任务,禁止在此post耗时操作
     *
     * @param runnable
     */
    public void runOnUiThread(Runnable runnable) {
        mMainHandler.post(runnable);
    }
    
    /**
     * 在Ui线程执行任务,禁止在此post耗时操作
     *
     * @param runnable
     */
    public void runOnUiThreadDelayed(Runnable runnable, long delay) {
        mMainHandler.postDelayed(runnable, delay);
    }
    
    public void removeUiThreadTask(Runnable runnable) {
        if (runnable != null) {
            mMainHandler.removeCallbacks(runnable);
        }
    }
    
    /**
     * 标准后台线程优先级，立即执行、不延时，绝大部分的后台线程都可在此线程池中执行
     *
     * @param runnable
     */
    public void runOnStdAsyncThread(Runnable runnable) {
        if (mScheduledPool != null && !mScheduledPool.isShutdown() && runnable != null) {
            mScheduledPool.schedule(runnable, 0, TimeUnit.MICROSECONDS);
        } else {
            String reason = "";
            if (mScheduledPool == null) {
                reason = "thread pool is null";
            } else if (mScheduledPool.isShutdown()) {
                reason = "thread pool is shut down";
            } else if (runnable == null) {
                reason = "runnable is null";
            }
            Log.i(TAG, "runOnStdAsyncThread: 出错");
        }
    }
    
    /**
     * 标准后台线程优先级，延迟循环执行
     *
     * @param runnable
     */
    public ScheduledFuture runOnStdAsyncThread(Runnable runnable, long period, long delay) {
        if (mScheduledPool != null && !mScheduledPool.isShutdown() && runnable != null) {
            return mScheduledPool.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
        } else {
            String reason = "";
            if (mScheduledPool == null) {
                reason = "thread pool is null";
            } else if (mScheduledPool.isShutdown()) {
                reason = "thread pool is shut down";
            } else if (runnable == null) {
                reason = "runnable is null";
            }
            Log.i(TAG, "runOnStdAsyncThread: 出错");
        }
        return null;
    }
    
    /**
     * 标准后台线程优先级，延时指定时间后执行，绝大部分的后台线程都可在此线程池中执行
     *
     * @param runnable
     * @param delay
     *            要延时的时间，单位为毫秒
     */
    public ScheduledFuture runOnStdAsyncThreadDelayed(Runnable runnable, long delay) {
        try {
            return mScheduledPool.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            Log.i(TAG, "runOnStdAsyncThreadDelayed: 出错");
        }
        return null;
    }
    
    /**
     * 需要在后台执行，但任务又非常紧急，这种情况下才可使用，普通的后台任务请使用runOnStdAsyncThread方法
     *
     * @param runnable
     */
    public void runOnUrgentAsyncThread(Runnable runnable) {
        mUrgentHandler.post(runnable);
    }
    
    /**
     * 需要在后台执行，但任务不紧急，不想占用线程池的资源，此时才可使用
     * 该线程的优先级最低，请慎用！！！
     *
     * @param runnable
     */
    public void runOnLowestAsyncThread(Runnable runnable) {
        mLowestHandler.post(runnable);
    }
    
    /**
     * 需要在后台执行，但任务不紧急，不想占用线程池的资源，此时才可使用
     * 该线程的优先级最低，请慎用！！！
     *
     * @param runnable
     * @param delay
     *            延时时间，单位为毫秒
     */
    public void runOnLowestAsyncThreadDelayed(Runnable runnable, long delay) {
        mLowestHandler.postDelayed(runnable, delay);
    }
    
    /**
     * 停止线程池、移除Handler中的Runnable
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stop() {
        if (mScheduledPool != null && !mScheduledPool.isShutdown()) {
            mScheduledPool.shutdown();
        }
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
        }
        if (mUrgentHandler != null) {
            mUrgentHandler.removeCallbacksAndMessages(null);
        }
        if (mUrgentHandlerThread != null) {
            mUrgentHandlerThread.quitSafely();
        }
        if (mLowestHandlerThread != null) {
            mLowestHandlerThread.quitSafely();
        }
        sInstance = null;
    }
    
    /**
     * 线程产生工厂,创建一个新线程：
     * 1. 设置线程名和线程组
     * 2. 设置线程为非守护线程（如果创建线程池的线程为守护线程，则其创建出的线程默认也是守护线程）
     * 3. 设置线程优先级为Android中标准的后台线程优先级
     */
    private static class NormalAsyncThreadFactory implements ThreadFactory {
        private final ThreadGroup mGroup;
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);
        private static final String NamePrefix = "browser_async_executor_";
        
        NormalAsyncThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            mGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }
        
        public Thread newThread(Runnable r) {
            String threadName = NamePrefix + mThreadNumber.getAndIncrement();
            Thread t = new Thread(mGroup, r, threadName, 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(PRIORITY_STD_BACKGROUND);
            return t;
        }
    }
}
