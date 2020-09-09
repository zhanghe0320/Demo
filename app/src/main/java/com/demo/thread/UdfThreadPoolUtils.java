package com.demo.thread;


import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户可自定义的线程池
 * Created by huangrenqian833 on 2018/11/13.
 */

public class UdfThreadPoolUtils {

    /**
     * 提供给udfThreadPoolExecutor方法使用
     */
    private static class EventThreadFactory implements ThreadFactory {
        private static final String PREFIX = "pa_udf_thread_pool_";
        private static AtomicInteger count = new AtomicInteger();
        private String threadName;// 线程名称
        private boolean threadDaemon;// 是否是守护线程

        public EventThreadFactory(String threadName, boolean threadDaemon) {
            this.threadName = threadName;
            this.threadDaemon = threadDaemon;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(PREFIX + threadName + "#" + count.getAndIncrement());
            thread.setDaemon(threadDaemon);

            return thread;
        }
    }

    /**
     * 用户可自定义的线程池方法
     *
     * @param corePoolSize    核心池的大小
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime   等待时间
     * @param threadName      线程名称
     * @param threadDaemon    是否是守护线程
     * @return ExecutorService
     */
    public static ExecutorService udfThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, String threadName, boolean threadDaemon) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                new EventThreadFactory(threadName, threadDaemon),
                new ThreadPoolExecutor.DiscardOldestPolicy());// 丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
    }

    /**
     * 用户可自定义的NewSingle线程池方法
     * 作用：只有一个线程的线程池，因此所有提交的任务是顺序执行
     *
     * @param threadName   线程名称
     * @param threadDaemon 是否是守护线程
     * @return ExecutorService
     */
    public static ExecutorService udfNewSingleThreadExecutor(final String threadName, final boolean threadDaemon) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, threadName);
                thread.setDaemon(threadDaemon);
                return thread;
            }
        });
    }

    /**
     * 用户可自定义的NewSingleScheduled线程池方法
     * 作用：只有一个线程，用来调度执行将来的任务
     *
     * @param threadName   线程名称
     * @param threadDaemon 是否是守护线程
     * @return ExecutorService
     */
    public static ScheduledExecutorService udfNewSingleThreadScheduledExecutor(final String threadName, final boolean threadDaemon) {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, threadName);
                thread.setDaemon(threadDaemon);
                return thread;
            }
        });
    }
}