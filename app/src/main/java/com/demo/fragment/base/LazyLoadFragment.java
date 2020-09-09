package com.demo.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.LogUtils;
import com.demo.mvp.BasePresenter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础封装
 */
public abstract class LazyLoadFragment<P extends BasePresenter> extends Fragment {
    private boolean isViewCreated; // 界面是否已创建完成
    private boolean isVisibleToUser; // 是否对用户可见
    private boolean isDataLoaded; // 数据是否已请求, isNeedReload()返回false的时起作用
    private boolean isHidden = true; // 记录当前fragment的是否隐藏
    
    protected Activity mActivity;// 获取activitry 指定cativity 绑定生命周期，防止因为生命周期问题导致保存崩溃
    protected Unbinder mUnbinder;// 绑定
    protected View mRootView;// view
    
    protected abstract @LayoutRes int initLayoutRes();// 获取布局
    
    protected abstract void initData();// 初始化数据
    
    protected abstract void initView();// 初始化view
    
    protected P mPresenter;// 控制器，内存泄漏,移除view
    
    protected abstract P createPresenter();
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        initData();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mRootView = inflater.inflate(initLayoutRes(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            LogUtils.i("onCreateView: 获取savedInstanceState");
            LogUtils.i(savedInstanceState.get("mess"));
            mRootView = inflater.inflate(initLayoutRes(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initView();
        }
        
        return mRootView;
    }
    
    // 实现具体的数据请求逻辑
    protected abstract void loadData();
    
    /**
     * 使用ViewPager嵌套fragment时，切换ViewPager回调该方法
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        tryLoadData();
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);// 保存状态
        isViewCreated = true;
        tryLoadData();
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    /**
     * 使用show()、hide()控制fragment显示、隐藏时回调该方法
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (!hidden) {
            tryLoadData1();
        }
    }
    
    /**
     * ViewPager场景下，判断父fragment是否可见
     *
     * @return
     */
    private boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        return fragment == null
                || (fragment instanceof LazyLoadFragment && ((LazyLoadFragment) fragment).isVisibleToUser);
    }
    
    /**
     * ViewPager场景下，当前fragment可见，如果其子fragment也可见，则尝试让子fragment加载请求
     */
    private void dispatchParentVisibleState() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof LazyLoadFragment && ((LazyLoadFragment) child).isVisibleToUser) {
                ((LazyLoadFragment) child).tryLoadData();
            }
        }
    }
    
    /**
     * fragment再次可见时，是否重新请求数据，默认为flase则只请求一次数据
     *
     * @return
     */
    protected boolean isNeedReload() {
        return false;
    }
    
    /**
     * ViewPager场景下，尝试请求数据
     */
    public void tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && (isNeedReload() || !isDataLoaded)) {
            loadData();
            isDataLoaded = true;
            dispatchParentVisibleState();
        }
    }
    
    /**
     * show()、hide()场景下，当前fragment没隐藏，如果其子fragment也没隐藏，则尝试让子fragment请求数据
     */
    private void dispatchParentHiddenState() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof LazyLoadFragment && !((LazyLoadFragment) child).isHidden) {
                ((LazyLoadFragment) child).tryLoadData1();
            }
        }
    }
    
    /**
     * show()、hide()场景下，父fragment是否隐藏
     *
     * @return
     */
    private boolean isParentHidden() {
        Fragment fragment = getParentFragment();
        if (fragment == null) {
            return false;
        } else if (fragment instanceof LazyLoadFragment && !((LazyLoadFragment) fragment).isHidden) {
            return false;
        }
        return true;
    }
    
    /**
     * show()、hide()场景下，尝试请求数据
     */
    public void tryLoadData1() {
        if (!isParentHidden() && (isNeedReload() || !isDataLoaded)) {
            loadData();
            isDataLoaded = true;
            dispatchParentHiddenState();
        }
    }
    
    @Override
    public void onDestroy() {
        isViewCreated = false;
        isVisibleToUser = false;
        isDataLoaded = false;
        isHidden = true;
        mUnbinder.unbind();// 销毁
        mRootView = null;// 置为空
        if (mPresenter != null) {// 防止崩溃
            mPresenter.detachView();// 移除view
        }
        super.onDestroy();
    }
}