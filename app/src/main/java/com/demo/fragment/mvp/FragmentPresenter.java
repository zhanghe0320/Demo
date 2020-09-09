package com.demo.fragment.mvp;

import android.app.Activity;

import com.demo.mvp.BasePresenter;

import java.util.List;

/**
 */
public class FragmentPresenter extends BasePresenter<FragmentContract.IFragmentView> implements FragmentContract.IFragmentPresenter {

    private FragmentContract.IFragmentModel mIFragmentModel;
    private FragmentContract.IFragmentView mIFragmentView;


    public FragmentPresenter(FragmentContract.IFragmentView fragment, Activity activity) {
        super(fragment);
        mIFragmentModel = new FragmentModel();
        mIFragmentView = fragment;
    }

    @Override
    public List Get(String mess) {
        //mIFragmentView.Get();
        return mIFragmentModel.Get(mess);
    }

    @Override
    public void Set(String mess) {
        mIFragmentModel.Set(mess);
        mIFragmentView.Set(mIFragmentModel.Get(mess));
    }

    @Override
    public void Change(String mess) {

        mIFragmentModel.Change(mess);
        mIFragmentView.Change(mIFragmentModel.Change(mess));

    }

    @Override
    public void Other(String mess) {
        mIFragmentModel.Other(mess);
        mIFragmentView.Other(mIFragmentModel.Other(mess));
    }


    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param mvpView
     */
    @Override
    public void attachView(FragmentContract.IFragmentView mvpView) {
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
    public FragmentContract.IFragmentView getView() {
        return super.getView();
    }





}
