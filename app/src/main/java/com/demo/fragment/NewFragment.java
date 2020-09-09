package com.demo.fragment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.fragment.base.LazyLoadFragment;
import com.demo.fragment.mvp.FragmentContract;
import com.demo.fragment.mvp.FragmentPresenter;

import java.util.List;

import butterknife.BindView;

/**
 */
public class NewFragment extends LazyLoadFragment<FragmentPresenter> implements FragmentContract.IFragmentView {
    @BindView(R.id.new_text)
    TextView newText;
    @BindView(R.id.new_recyclerView)
    RecyclerView mNewRecyclerView;

    public NewFragment() {
    }

    public static NewFragment newInstance(String data) {
        NewFragment fragment = new NewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected int initLayoutRes() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        newText.setText("新闻");


    }

    @Override
    protected FragmentPresenter createPresenter() {
        return new FragmentPresenter(this,getActivity());
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
    }

    @Override
    public void Change(List list) {
    }

    @Override
    public void Other(List list) {
    }
}