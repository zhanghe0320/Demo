package com.demo.mvp;

/**
 * 单独作用的数据
 * 验证是否存在，不存在的时候网络请求数据，或者数据数据获取之后，无法更新UI
 * 此时判断不进行UI更新，防止崩溃，用于管理UI的数据请求延迟崩溃问题
 */
public class BasePresenter<V extends BaseView> {
    
    public BasePresenter(V baseView) {
        super();
        this.mvpView = baseView;
    }
    
    /**
     * 绑定的view
     */
    private V mvpView;
    
    /**
     * 绑定view，一般在初始化中调用该方法
     */
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }
    
    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mvpView = null;
    }
    
    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return mvpView != null;
    }
    
    /**
     * 获取连接的view
     */
    public V getView() {
        return mvpView;
    }
    
}
