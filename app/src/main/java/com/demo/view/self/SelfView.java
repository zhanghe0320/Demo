package com.demo.view.self;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 自定义view
 */
public class SelfView extends View {
    
    /***
     * java文件调用此方法
     * 
     * @param context
     */
    public SelfView(Context context) {
        super(context);
    }
    
    /**
     * xml 文件调用此方法
     * 
     * @param context
     * @param attrs
     */
    public SelfView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SelfView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelfView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    /**
     * 分析1：onMeasure（）
     * 作用：
     * a. 根据View宽/高的测量规格计算View的宽/高值：getDefaultSize()
     * b. 存储测量后的View宽 / 高：setMeasuredDimension()
     * 测量
     * 
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
