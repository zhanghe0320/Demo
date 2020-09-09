package com.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.demo.mvp.BasePresenter;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements CustomAdapt {
    
    protected P mPresenter;
    
    protected abstract P createPresenter();
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.addActivity(this);
        mPresenter = createPresenter();
        setStatusBar();
        ActivityUtils.addActivityLifecycleCallbacks(this, new Utils.ActivityLifecycleCallbacks());
        
    }
    
    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {
        // StatusBarUtil.setTranslucentForDrawerLayout(this,null);
        // StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        // StatusBarUtil.setTransparentForWindow(this);
        getSupportActionBar().hide(); // 隐藏标题栏
     //   BarUtils.setNotificationBarVisibility(true);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏幕显示
    }
    
    @Override
    protected void onStart() {
        super.onStart();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
         BaseApplication.removeActivity(this);
        //ActivityUtils.finishActivity(this);
        super.onDestroy();
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
