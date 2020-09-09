package com.demo.drawable.ripple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 涟漪效果
 */
public class RippleDrawable extends Drawable {
    private Paint mPaint;
    private int mRadius;
    private int mWidth;
    private int mHeight;
    private int mMaxRadius;
    private int mPivotX;
    private int mPivotY;
    private int mMotionX;
    private int mMotionY;
    private float mProgress = 0.0f;
    private int mAlpha = 255;

    public RippleDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.CYAN);
        mPaint.setStyle(Paint.Style.FILL);
    }

    // 绘制圆形
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(mPivotX, mPivotY, mRadius, mPaint);
    }

    // 记录当前Drawable的尺寸
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mWidth = bounds.width();
        mHeight = bounds.height();
        mMaxRadius = Math.min(mWidth, mHeight);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    // 根据ACTION_DOWN展示圆心位置
    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onTouchUp(event);
                break;
        }
    }

    private void onTouchUp(MotionEvent event) {

    }

    private void onTouchMove(MotionEvent event) {

    }

    private void onTouchDown(MotionEvent event) {
        mMotionX = (int) event.getX();
        mMotionY = (int) event.getY();
        mRadius = 0;
        mProgress = 0.0f;
        mAlpha = 255;
        setAlpha(mAlpha);
        unscheduleSelf(mEnterRunnable);

        // 每10ms开始一次重绘
        scheduleSelf(mEnterRunnable, SystemClock.uptimeMillis() + 10);
    }

    private Interpolator interpolator = new AccelerateInterpolator();

    private Runnable mEnterRunnable = new Runnable() {
        @Override
        public void run() {
            // 如果重绘完成，开始执行退出动画
            if (mProgress >= 1.0f) {
                unscheduleSelf(mExitRunnable);
                scheduleSelf(mExitRunnable, SystemClock.uptimeMillis() + 10);
                return;
            }

            // 不断增大圆形半径
            mProgress += 0.05f;
            mRadius = (int) (mMaxRadius * interpolator.getInterpolation(mProgress));
            mPivotX = mMotionX;
            mPivotY = mMotionY;
            invalidateSelf();
            scheduleSelf(mEnterRunnable, SystemClock.uptimeMillis() + 10);
        }
    };

    // 退出动画
    private Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            int alpha = mAlpha - 10;
            if (alpha > 0) {
                // 从不透明变成完全透明实现Drawable内容不可见
                mAlpha = alpha;
                setAlpha(alpha);
                invalidateSelf();
                scheduleSelf(this, SystemClock.uptimeMillis() + 10);
            } else {
                mAlpha = 0;
                setAlpha(mAlpha);
                invalidateSelf();
            }
        }
    };
}
