package com.demo.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 */
public  abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;
    protected Unbinder mUnbinder;
    protected View mRootView;

    protected abstract @LayoutRes
    int initLayoutRes();

    protected abstract void initData();

    protected abstract void initView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(initLayoutRes(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
