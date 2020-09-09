package com.demo.view.status;

import android.app.Activity;

/**
 */
public interface StatusBarContract {
    /**
     * view
     */
    interface IStatusBarRelativeLayout {
        void Back(Activity activity);
        
        void MainSet(Activity activity);
    }
    
    /**
     * 控制器
     */
    interface IStatusBarPresenter {
        
        void Back(Activity activity);
        
        void MainSet(Activity activity);
        
    }
}
