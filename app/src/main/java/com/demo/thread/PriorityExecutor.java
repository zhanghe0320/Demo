package com.demo.thread;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 支持优先级的线程池管理类
 *
 * @author 优化取自xUitls的线程池工具类
 */
public class PriorityExecutor implements Executor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    private static final int CORE_POLL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POLL_SIZE = CPU_COUNT * 2 + 1;
    private static final int WORK_QUEUE_SIZE = 160;// TODO AsyncTask的是128，后期选定一个折中值
    private static final int KEEP_ALIVE = 1;
    private static final AtomicInteger SEQ_SEED = new AtomicInteger(0);

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "xTID#" + mCount.getAndIncrement());
        }
    };

    private static final Comparator<Runnable> FIFO_CMP = new Comparator<Runnable>() {

        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable &&
                    rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = (PriorityRunnable) lhs;
                PriorityRunnable rpr = (PriorityRunnable) rhs;
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

    private static final Comparator<Runnable> FILO_CMP = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable &&
                    rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = (PriorityRunnable) lhs;
                PriorityRunnable rpr = (PriorityRunnable) rhs;
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

    private final ThreadPoolExecutor mThreadPoolExecutor;

    public PriorityExecutor(boolean fifo) {
        this(CORE_POLL_SIZE, fifo);
    }

    public PriorityExecutor(int poolSize, boolean fifo) {
        BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue<>(WORK_QUEUE_SIZE, fifo ? FIFO_CMP : FILO_CMP);
        mThreadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                MAXIMUM_POLL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                mPoolWorkQueue,
                sThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());// 不用abort，避免崩溃
        // TODO: 2018/11/21 注释掉这个，是因为开启了保存日志功能会出现死循环
//        PALog.w("PriorityExecutor", "CPU_COUNT:" + CPU_COUNT + "，CORE_POLL_SIZE:" + CORE_POLL_SIZE
//                + "，MAXIMUM_POLL_SIZE:" + MAXIMUM_POLL_SIZE + "，WORK_QUEUE_SIZE:" + WORK_QUEUE_SIZE + "，KEEP_ALIVE:" + KEEP_ALIVE + "s");
    }

    public int getPoolSize() {
        return mThreadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            mThreadPoolExecutor.setCorePoolSize(poolSize);
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mThreadPoolExecutor;
    }

    public boolean isBusy() {
        return mThreadPoolExecutor.getActiveCount() >= mThreadPoolExecutor.getCorePoolSize();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (runnable instanceof PriorityRunnable) {
            ((PriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        mThreadPoolExecutor.execute(runnable);
    }

    // -----------------------------------以下为扩展的方法-------------------------------------

    /**
     * submit与execute区别接收1.参数不一样，2.有返回值，3.方便Exception处理
     * future.cancel(true);
     * future.isCancelled();
     * future.isDone();
     *
     * @param runnable runnable
     * @return Future
     */
    public Future<?> submit(@NonNull Runnable runnable) {
        if (runnable instanceof PriorityRunnable) {
            ((PriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        return mThreadPoolExecutor.submit(runnable);
    }
}