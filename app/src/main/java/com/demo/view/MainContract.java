package com.demo.view;

import com.demo.mvp.BaseView;

/**
 */
public interface MainContract {
    /**
     * 数据管理
     */
    interface IMainModel {
        String CreateFragment(String name);
        
    }
    
    /**
     * View
     */
    interface IMainView extends BaseView {
        /**
         * 设置 初始化
         *
         * @param mess
         */
        void CreateFragment(String mess);
        
        /*
         * 切换
         */
        void ChangeFragment(String mess);
    }
    
    /**
     * 控制器
     */
    interface IMainPresenter {
        void createFragment(String mess);
        
        void ChangeFragment(String mess);
        
        // Presenter.java
        // void detachView();
        
    }
}
