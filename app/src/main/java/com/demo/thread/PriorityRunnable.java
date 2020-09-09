package com.demo.thread;



/**
 * 带有优先级的Runnable类型
 *
 * @author 优化取自xUitls的线程池工具类
 */
public class PriorityRunnable implements Runnable {

    long SEQ;

    public final Priority priority;
    private final Runnable runnable;

    public PriorityRunnable(Priority priority, Runnable runnable) {
        this.priority = priority == null ? Priority.DEFAULT : priority;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}
