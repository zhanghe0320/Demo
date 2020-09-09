package com.demo.drawable.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 涟漪按钮
 */
public class RippleButton extends AppCompatTextView {
    private RippleDrawable mDrawable;
    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 自定义的涟漪效果Drawable
        mDrawable = new RippleDrawable();
        mDrawable.setCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 在View大小改变的时候设置Drawable大小
        mDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制Drawable内容
        mDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return mDrawable == who || super.verifyDrawable(who);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 拦截用户触摸事件
        mDrawable.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }
}
