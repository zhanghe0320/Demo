package com.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.demo.R;
import com.demo.activity.ItemMessActivity;
import com.demo.fragment.base.FragmentButton;
import com.demo.fragment.base.LazyLoadFragment;
import com.demo.fragment.dao.FragmentBean;
import com.demo.fragment.mvp.FragmentContract;
import com.demo.fragment.mvp.FragmentPresenter;
import com.demo.fragment.view.FragmentAdapter;
import com.demo.view.ConstantValue;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 */
public class AllFragment extends LazyLoadFragment<FragmentPresenter>
        implements FragmentContract.IFragmentView, FragmentAdapter.AdapterItemOnclick {
    @BindView(R.id.fragment_btn)
    FragmentButton mFragmentButton;
    @BindView(R.id.all_recyclerView)
    RecyclerView mAllRecyclerView;
    @BindView(R.id.all_text)
    TextView allText;
    
    // private FragmentPresenter fragmentPresenter;
    
    private FragmentAdapter mFragmentAdapter;
    
    private List mData = new ArrayList();
    
    public AllFragment() {
        // Required empty public constructor
    }
    
    public static AllFragment newInstance(String data) {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    
    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        mData.clear();
        mData = mPresenter.Get("初始化");
        mFragmentAdapter.setData(mData);
        allText.setText("所有");
        mFragmentButton.RefreshView();
    }
    
    /***
     * fragment 赋值
     * 
     * @return
     */
    @Override
    protected int initLayoutRes() {
        
        return R.layout.fragment_all;
    }
    
    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        // mPresenter = new FragmentPresenter(this, getActivity());
        mData = mPresenter.Get("初始化");
        // mFragmentButton.RefreshView();
    }
    
    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        mPresenter.attachView(this);//view 注册
        allText.setText("所有");
        // fragmentPresenter = new FragmentPresenter(this, getActivity());
        mData = mPresenter.Get("初始化");
        mFragmentAdapter = new FragmentAdapter(getActivity(), ConstantValue.ALL, mData, this);
        mFragmentAdapter.setData(mData);
        mAllRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAllRecyclerView.setAdapter(mFragmentAdapter);
        
        mFragmentButton.setiFragmentPresenter(this, getActivity());
        
    }
    
    @Override
    protected FragmentPresenter createPresenter() {
        return new FragmentPresenter(this, getActivity());
    }
    
    /**
     * fragment再次可见时，是否重新请求数据，默认为flase则只请求一次数据
     *
     * @return
     */
    @Override
    protected boolean isNeedReload() {
        return true;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void TitleOnclick() {
        // 仅用户传递事件 ，不做数据处理
        mPresenter.Change("标题点击事件");
    }
    
    @Override
    public void TextOncklick() {
        // 仅用户传递事件 ，不做数据处理
        // 可以进行model 请求数据 或者调用presenter
        mPresenter.Change("文字点击事件");
    }
    
    @Override
    public void BtnOnclick() {
        // 仅用户传递事件 ，不做数据处理
        // 直接使用presenter 处理
        mPresenter.Change("按钮点击事件");
    }
    
    @Override
    public void ItemOnclick(FragmentBean fragmentBean) {// 仅用户传递事件 ，不做数据处理
        Intent intent = new Intent();
        intent.setClass(getActivity(), ItemMessActivity.class);
        intent.putExtra("fragmentBean", fragmentBean);
        startActivity(intent);
    }
    
    @Override
    public void Get() {
        ToastUtils.show("get事件处理");
    }
    
    @Override
    public void Set(List list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragmentAdapter.setData(list);
            }
        });
    }
    
    @Override
    public void Change(List list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                
                mFragmentAdapter.setData(list);
            }
        });
    }
    
    @Override
    public void Other(List list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                
                mFragmentAdapter.setData(list);
            }
        });
    }


}
