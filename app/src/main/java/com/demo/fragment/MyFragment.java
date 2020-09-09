package com.demo.fragment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.fragment.base.FragmentButton;
import com.demo.fragment.base.LazyLoadFragment;
import com.demo.fragment.mvp.FragmentContract;
import com.demo.fragment.mvp.FragmentPresenter;
import com.demo.fragment.view.FragmentAdapter;
import com.demo.view.ConstantValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 */
public class MyFragment extends LazyLoadFragment<FragmentPresenter> implements FragmentContract.IFragmentView {
    @BindView(R.id.my_text)
    TextView myText;
    @BindView(R.id.fragment_btn)
    FragmentButton mFragmentButton;
    @BindView(R.id.my_recyclerView)
    RecyclerView mMyRecyclerView;
    
    // private FragmentPresenter mFragmentPresenter;
    private FragmentAdapter mFragmentAdapter;
    
    private List mData = new ArrayList();
    
    public MyFragment() {
    }
    
    public static MyFragment newInstance(String data) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected void loadData() {
        mData = mPresenter.Get("获取");
    }
    
    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_my;
    }
    
    @Override
    protected void initData() {
        // mFragmentPresenter = new FragmentPresenter(this, getActivity());
        mData = mPresenter.Get("获取");
    }
    
    @Override
    protected void initView() {
        myText.setText("我的");
        
        // mFragmentPresenter = new FragmentPresenter(this, getActivity());
        mData = mPresenter.Get("获取");
        
        mFragmentAdapter = new FragmentAdapter(getActivity(), ConstantValue.ALL, mData);
        mFragmentAdapter.setData(mData);
        mMyRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mMyRecyclerView.setAdapter(mFragmentAdapter);
        
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
        return super.isNeedReload();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void Get() {
        
    }
    
    @Override
    public void Set(List list) {
        if (mPresenter.isViewAttached()) {// 界面是否存在
            mFragmentAdapter.setData(list);
        }
    }
    
    @Override
    public void Change(List list) {
        mFragmentAdapter.setData(list);
    }
    
    @Override
    public void Other(List list) {
        mFragmentAdapter.setData(list);
    }
}