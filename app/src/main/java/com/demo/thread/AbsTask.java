package com.demo.thread;



import java.util.concurrent.Executor;

/**
 * 异步任务基类
 *
 * @author 优化取自xUitls的线程池工具类
 */
public abstract class AbsTask<ResultType> implements Callback.Cancelable {

    private TaskProxy taskProxy = null;
    private final Callback.Cancelable cancelHandler;

    private volatile boolean isCancelled = false;
    private volatile State state = State.IDLE;
    private ResultType result;

    public AbsTask() {
        this(null);
    }

    public AbsTask(Callback.Cancelable cancelHandler) {
        this.cancelHandler = cancelHandler;
    }

    protected abstract ResultType doBackground() throws Throwable;

    protected abstract void onSuccess(Object result);

    protected abstract void onError(Throwable ex, boolean isCallbackError);

    protected void onWaiting() {

    }

    protected void onStart() {

    }

    protected void onUpdate(int flag, Object... args) {

    }

    protected void onCancelled(Callback.CancelledException cex) {

    }

    protected void onFinished() {

    }

    public Priority getPriority() {
        return null;
    }

    public Executor getExecutor() {
        return null;
    }

    protected final void update(int flag, Object... agrs) {
        if (taskProxy != null) {
            taskProxy.onUpdate(flag, agrs);
        }
    }

    protected void cancelWorks() {
    }

    private boolean isCancelFast() {
        return false;
    }


    @Override
    public final synchronized void cancel() {
        if (!this.isCancelled) {
            this.isCancelled = true;
            cancelWorks();
            if (cancelHandler != null && !cancelHandler.isCancelled()) {
                cancelHandler.cancel();
            }
            if (this.state == state.WAITING ||
                    (this.state == State.STARTED && isCancelFast())) {
                if (taskProxy != null) {
                    taskProxy.onCancelled(new Callback.CancelledException("cancelled by user"));
                    taskProxy.onFinished();
                } else if (this instanceof TaskProxy) {
                    this.onCancelled(new Callback.CancelledException("cancelled by user"));
                    this.onFinished();
                }
            }
        }
    }

    @Override
    public final boolean isCancelled() {
        return isCancelled || state == State.CANCELLED ||
                (cancelHandler != null && cancelHandler.isCancelled());
    }

    public final boolean isFinished() {
        return this.state.value() > state.STARTED.value();
    }

    public final State getState() {
        return state;
    }

    public final ResultType getResult() {
        return result;
    }

    void setState(State state) {
        this.state = state;
    }

    final void setTaskProxy(TaskProxy taskProxy) {
        this.taskProxy = taskProxy;
    }

    final void setResult(ResultType result) {
        this.result = result;
    }

    public enum State {
        IDLE(0), WAITING(1), STARTED(2), SUCCESS(3), CANCELLED(4), ERROR(5);
        private final int value;

        State(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

    }
}