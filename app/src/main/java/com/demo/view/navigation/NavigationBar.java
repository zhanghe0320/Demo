package com.demo.view.navigation;

import java.util.List;

public interface NavigationBar {
    /**
     */
    interface INavigationBarModel {
        List getAll();
    }
    
    /**
     */
    interface INavigationBarPresenter {
        void setText(List list);
    }
    
    /**
     * 导航栏
     */
    interface INavigationBarRelativeLayout {
        void setText(List list);
    }
    
    /**
     * 控制界面跳转
     * 提供给外界使用的接口
     * 提高导航栏通用性
     * 只用于传递五个点击事件到view 然后 通过view 持有的控制器进行相关操作
     */
    interface NavigationPresenter {
        void oneOnclick(/* NavigationPresenter navigationPresenter, String mess */);
        
        void twoOnclick(/* NavigationPresenter navigationPresenter, String mess */);
        
        void threeOnclick(/* NavigationPresenter navigationPresenter, String mess */);
        
        void fourOnclick(/* NavigationPresenter navigationPresenter, String mess */);
        
        void fiveOnclick(/* NavigationPresenter navigationPresenter, String mess */);
    }
    
}
