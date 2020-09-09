package com.demo.fragment.mvp;

import com.demo.mvp.BaseView;

import java.util.List;

/**
 */
public interface FragmentContract {
    /**
     * 数据管理
     */
    interface IFragmentModel {
        List Get(String mess);
        
        void Set(String mess);
        
        List Change(String mess);
        
        List Other(String mess);

        
    }
    
    /**
     * view
     */
    interface IFragmentView extends BaseView {
        void Get();
        
        void Set(List list);
        
        void Change(List list);
        
        void Other(List list);
        
    }
    
    /**
     * 按钮控制器
     */
    interface IFragmentPresenter {
        List Get(String mess);
        
        void Set(String mess);
        
        void Change(String mess);
        
        void Other(String mess);
        
    }
}
