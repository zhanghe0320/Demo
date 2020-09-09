package com.demo.thread;



/**
 * 通用回调接口
 *
 * @author 优化取自xUitls的线程池工具类
 */
public interface Callback {

    interface GroupCallback<ItemType> extends Callback {

        void onSuccess(ItemType item);

        void onError(ItemType item, Throwable ex, boolean isOnCallback);

        void onCancelled(ItemType item, CancelledException cex);

        void onFinished(ItemType item);

        void onAllFinished();
    }

    interface Cancelable {
        void cancel();

        boolean isCancelled();
    }

    class CancelledException extends RuntimeException {

        public CancelledException(String detailMessage) {
            super(detailMessage);
        }

    }

}