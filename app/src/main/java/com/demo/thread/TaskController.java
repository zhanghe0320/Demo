package com.demo.thread;



import android.app.Activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 任务管理接口
 *
 * @author 优化取自xUitls的线程池工具类
 */
public interface TaskController {

    /**
     * 在UI线程执行Runnable
     * 如果已经在UI线程，则直接执行
     *
     * @param runnable runnable
     */
    void autoPost(Runnable runnable);

    /**
     * 在UI线程执行Runnable
     * post到msg queue
     *
     * @param runnable runnable
     */
    void post(Runnable runnable);

    /**
     * 在UI线程执行Runnable
     *
     * @param runnable    runnable
     * @param delayMillis 延迟时间（单位毫秒）
     */
    void postDelayed(Runnable runnable, long delayMillis);

    /**
     * 在后台线程执行Runnable
     *
     * @param runnable runnable
     */
    void execute(Runnable runnable);

    /**
     * 直接开一个"可缓存线程池"去跑一个线程，该方法开超过4百多线程，APP就会OOM崩溃，所以弃用。
     *
     * @param runnable runnable
     */
    void executeNewCached(Runnable runnable);

    /**
     * 在后台线程执行Runnable
     *
     * @param runnable runnable
     */
    Future<?> submit(Runnable runnable);

    /**
     * 移除post或postDelayed提交的，为执行的任务
     *
     * @param runnable runnable
     */
    void removeCallbacks(Runnable runnable);

    void removeCallbacks(Runnable... runnable);

    /**
     * 开始一个异步任务(不考虑内存泄漏)
     *
     * @param task task
     * @param <T>  <T>
     * @return AbsTask<T>
     */
    <T> AbsTask<T> start(AbsTask<T> task);

    /**
     * 开始一个异步任务(考虑内存泄漏)
     *
     * @param task task
     * @param <T>  <T>
     * @return AbsTask<T>
     */
    <T> AbsTask<T> start(Activity activity, AbsTask<T> task);

    /**
     * 开始一个同步任务
     *
     * @param task task
     * @param <T>  <T>
     * @return T
     * @throws Throwable
     */
    <T> T startSync(AbsTask<T> task) throws Throwable;

    /**
     * 批量执行异步任务
     *
     * @param groupCallback groupCallback
     * @param tasks         tasks
     * @param <T>           <T>
     * @return <T extends AbsTask<?>>
     */
    <T extends AbsTask<?>> Callback.Cancelable startTasks(Callback.GroupCallback<T> groupCallback, T... tasks);


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
    ExecutorService udfThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, String threadName, boolean threadDaemon);

    /**
     * 用户可自定义的NewSingle线程池方法
     *
     * @param threadName   线程名称
     * @param threadDaemon 是否是守护线程
     * @return ExecutorService
     */
    ExecutorService udfNewSingleThreadExecutor(final String threadName, final boolean threadDaemon);

    /**
     * 用户可自定义的NewSingleScheduled线程池方法
     * 作用：只有一个线程，用来调度执行将来的任务
     *
     * @param threadName   线程名称
     * @param threadDaemon 是否是守护线程
     * @return ExecutorService
     */
    ScheduledExecutorService udfNewSingleThreadScheduledExecutor(final String threadName, final boolean threadDaemon);
}
