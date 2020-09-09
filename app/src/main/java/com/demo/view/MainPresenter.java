package com.demo.view;

import com.demo.mvp.BasePresenter;
import com.demo.view.navigation.NavigationBar;

/**
 * 继承导航控制器，做处理，跳转
 */
public class MainPresenter extends BasePresenter<MainContract.IMainView> implements MainContract.IMainPresenter, NavigationBar.NavigationPresenter {
    
    private MainContract.IMainModel iMainModel;
    private MainContract.IMainView iMainView;
    
    public MainPresenter(MainContract.IMainView mainView) {
        super(mainView);
        iMainView = mainView;
        iMainModel = new MainModel();
    }
    
    @Override
    public void createFragment(String mess) {
        String name = iMainModel.CreateFragment(mess);
        switch (mess) {
        case ConstantValue.WEB:
            iMainView.CreateFragment(name);
            break;
        case ConstantValue.NEW:
            iMainView.CreateFragment(name);
            break;
        case ConstantValue.My:
            iMainView.CreateFragment(name);
            break;
        case ConstantValue.ALL:
            iMainView.CreateFragment(name);
            break;
        case ConstantValue.TEXT:
            iMainView.CreateFragment(name);
            break;
        }
    }
    
    @Override
    public void ChangeFragment(String mess) {
        switch (mess) {
        case ConstantValue.WEB:
            iMainView.ChangeFragment(mess);
            break;
        case ConstantValue.NEW:
            iMainView.ChangeFragment(mess);
            break;
        case ConstantValue.My:
            iMainView.ChangeFragment(mess);
            break;
        case ConstantValue.ALL:
            iMainView.ChangeFragment(mess);
            break;
        case ConstantValue.TEXT:
            iMainView.ChangeFragment(mess);
            break;
        }
        
    }
    
    /**
     * 导航栏按钮点击事件处理，五个按钮点击事件
     *
     */
    @Override
    public void oneOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ChangeFragment(ConstantValue.ALL);
        // iMainView.ChangeFragment(mess); // mess 为ConstantValue.ALL
    }
    
    @Override
    public void twoOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ChangeFragment(ConstantValue.NEW);
    }
    
    @Override
    public void threeOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ChangeFragment(ConstantValue.TEXT);
    }
    
    @Override
    public void fourOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ChangeFragment(ConstantValue.WEB);
    }
    
    @Override
    public void fiveOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        if (isViewAttached()) {
            ChangeFragment(ConstantValue.My);
        }
        
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param mvpView
     */
    @Override
    public void attachView(MainContract.IMainView mvpView) {
        super.attachView(mvpView);
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    @Override
    public boolean isViewAttached() {
        return super.isViewAttached();
    }

    /**
     * 获取连接的view
     */
    @Override
    public MainContract.IMainView getView() {
        return super.getView();
    }
}
