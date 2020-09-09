package com.demo.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ThreadUtils;
import com.demo.R;
import com.demo.fragment.mvp.FragmentContract;
import com.demo.fragment.mvp.FragmentPresenter;
import com.demo.view.ConstantValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * 上面的控制器
 */
public class FragmentButton extends RelativeLayout implements View.OnClickListener, CustomAdapt {

    @BindView(R.id.fragment_btn_change)
    TextView mFragmentBtnChange;
    @BindView(R.id.fragment_btn_get)
    TextView mFragmentBtnGet;
    @BindView(R.id.fragment_btn_set)
    TextView mFragmentBtnSet;
    @BindView(R.id.fragment_btn_other)
    TextView mFragmentBtnOther;

    private String mType = ConstantValue.ALL;


    private FragmentContract.IFragmentPresenter iFragmentPresenter;

    public void setiFragmentPresenter(FragmentContract.IFragmentView allFragment, Activity activity) {
        mType = ConstantValue.ALL;
        iFragmentPresenter = new FragmentPresenter(allFragment, activity);
    }


    public FragmentButton(Context context) {
        super(context);
    }

    public FragmentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FragmentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FragmentButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initData(AttributeSet attrs) {

    }


    private void init(Context Context, AttributeSet attrs) {
        inflate(Context, R.layout.relativelayout_fragment_btn, this);
        ButterKnife.bind(this);

        mFragmentBtnChange.setOnClickListener(this::onClick);
        mFragmentBtnGet.setOnClickListener(this::onClick);
        mFragmentBtnSet.setOnClickListener(this::onClick);
        mFragmentBtnOther.setOnClickListener(this::onClick);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_btn_change:
                Change();
                break;
            case R.id.fragment_btn_set:
                Set();
                break;
            case R.id.fragment_btn_get:
                Get();
                mFragmentBtnGet.post(new Runnable() {
                    @Override
                    public void run() {
                        mFragmentBtnGet.setText("点击");
                    }
                });
                break;
            case R.id.fragment_btn_other:
                Other();
                break;
            default:
                break;
        }
    }

    void Change() {
        iFragmentPresenter.Change("改变");
    }

    void Set() {
        iFragmentPresenter.Set("设置");
    }


    void Get() {
        iFragmentPresenter.Get("获取");
    }

    void Other() {
        iFragmentPresenter.Other("其他");
    }

    public void RefreshView(){
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragmentBtnChange.setText("改变");
                mFragmentBtnGet.setText("获");
                mFragmentBtnSet.setText("设置");
                mFragmentBtnOther.setText("其他");
            }
        });

    }

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度适配, {@code false} 为按照高度适配
     */
    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    /**
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
    @Override
    public float getSizeInDp() {
        return 360;
    }
}
