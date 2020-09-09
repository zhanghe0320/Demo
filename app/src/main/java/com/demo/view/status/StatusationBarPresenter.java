package com.demo.view.status;

import android.app.Activity;

/**
 */
public class StatusationBarPresenter implements StatusBarContract.IStatusBarPresenter {
    private StatusBarContract.IStatusBarRelativeLayout mStatusBarRelativeLayout;

    public StatusationBarPresenter(StatusBarContract.IStatusBarRelativeLayout statusBarRelativeLayout) {
        super();
        this.mStatusBarRelativeLayout = statusBarRelativeLayout;
    }

    @Override
    public void Back(Activity activity) {
        mStatusBarRelativeLayout.Back(activity);
    }


    @Override
    public void MainSet(Activity activity) {
        mStatusBarRelativeLayout.MainSet(activity);
    }
}

