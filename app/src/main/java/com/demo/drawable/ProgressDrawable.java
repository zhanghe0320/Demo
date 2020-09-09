package com.demo.drawable;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
 * R.drawable.mv);
 * ImageView iv = (ImageView) findViewById(R.id.id_one);
 * iv.setImageDrawable(new RoundImageDrawable(bitmap));
 */
public class ProgressDrawable extends Drawable implements Animatable {
    private Paint mPaint;
    private Paint mPaintBehind;
    private Paint mPaintFirst;
    private Path mPath;
    private Bitmap mBitmap;
    
    private int mWidth;
    private int mHeight;
    
    // 目标View的宽高的一半
    private int mHalfWidth;
    private int mHalfHeight;
    // 扩散半径
    private int mRadius;
    private int mRadius2;
    // 前景色和背景色的分割距离
    private int mDivideSpace;
    // 扩散满视图需要的距离，中点到斜角的距离
    private int mFullSpace;
    // 动画控制
    private ValueAnimator mValueAnimator;
    // 绘制的矩形框
    private RectF mRect = new RectF();
    // 动画启动延迟时间
    private int mStartDelay;
    
    private Canvas mCanvas;
    
    public ProgressDrawable(Bitmap bitmap, int width, int height) {
        mBitmap = bitmap;
        // BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
        // Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaintBehind = new Paint();
        mPaintBehind.setAntiAlias(true);
        mPaintFirst = new Paint();
        mPaintFirst.setAntiAlias(true);
        
        // mPaint.setShader(bitmapShader);
        
        this.mHeight = height;
        this.mWidth = width;
        mHalfWidth = mWidth;
        mHalfHeight = mHeight;
        
    }
    
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }
    
    @Override
    public void draw(Canvas canvas) {
        this.mCanvas = canvas;
        final Rect rect = getBounds();
        rect.set(0, 0, mWidth, mHeight);
        // LinearGradient 第一个参数第二个参数为 起始位置x,y 三四参数为终点位置x,y。
        // 如果x不变则为y轴渐变， y不变则为x轴渐变
        // 第五个参数为颜色渐变，此处为红色渐变为绿色
        // 第七个参数为渐变次数，可repeat
        int count = 0;
        // 底色
        Shader mShader2 = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.GRAY, Color.CYAN }, null,
                Shader.TileMode.CLAMP);
        mPaintBehind.setShader(mShader2);
        count = mCanvas.save();
        Path path2 = new Path();
        path2.moveTo(mWidth - 30, mHeight);
        path2.lineTo(mWidth, 0);
        path2.lineTo(0, 0);
        path2.lineTo(0, mHeight);
        path2.close();
        mCanvas.drawPath(path2, mPaintBehind);
        mCanvas.restoreToCount(count);




        // 动画
        Shader mShader = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.BLUE, Color.RED }, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        count = mCanvas.save();
        Path path = new Path();
        path.moveTo(mRadius - 30, mHeight);// 开始
        path.lineTo(mRadius, 0);// 终点
        path.lineTo(0, 0);// 中调
        path.lineTo(0, mHeight);

        // path.moveTo(0, mHeight);//开始
        // path.lineTo(0, mWidth);//终点
        // path.lineTo(mRadius, mHeight);//中调
        // path.lineTo(0, mHeight);
        // path.moveTo(mWidth, 0);//开始
        // path.lineTo(0, mHeight);//终点
        // path.lineTo(0, mHeight);//中调
        // path.lineTo(mRadius - 10, 0);
        path.close();
        mCanvas.drawPath(path, mPaint);
        mCanvas.restoreToCount(count);
        LogUtils.i("动画");
        // 动画
        // Shader mShader3 = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.WHITE, Color.GREEN
        // }, null,
        // Shader.TileMode.CLAMP);
        // mPaintFirst.setShader(mShader3);
        // count = mCanvas.save();
        // Path path3 = new Path();
        // path3.moveTo(mRadius2 - 30, mHeight);// 开始
        // path3.lineTo(mRadius2, 0);// 终点
        // path3.lineTo(0, 0);// 中调
        // path3.lineTo(0, mHeight);
        // path3.close();
        // mCanvas.drawPath(path3, mPaintFirst);
        // mCanvas.restoreToCount(count);
        
//        for (int i = 0; i < 10; i++) {
//            Bean bean = (Bean) list.get(i);
//
//            // 动画
//
//            bean.paint.setShader(bean.mShader);
//            count = mCanvas.save();
//            bean.path.moveTo(bean.mRadius - 30, mHeight);// 开始
//            bean.path.lineTo(bean.mRadius, 0);// 终点
//            bean.path.lineTo(0, 0);// 中调i
//            bean.path.lineTo(0, mHeight);
//            bean.path.close();
//            mCanvas.drawPath(bean.path, bean.paint);
//            mCanvas.restoreToCount(count);
//
//        }

    }
    
    @Override
    public int getIntrinsicWidth() {
        return mWidth;// mBitmap.getWidth();
    }
    
    @Override
    public int getIntrinsicHeight() {
        return mHeight;// mBitmap.getHeight();
    }
    
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
    
    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
    
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
    
    private Rect rect;
    List list = new ArrayList();
    
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.rect = bounds;
        // int w = bounds.width();
        // int h = bounds.height();
        // int min = Math.min(w, h);
        // int cx = bounds.centerX();
        // int cy = bounds.centerY();
        // int r = min / 2;
        //
        // mRect = new RectF(cx - r, cy - r, cx + r, cy + r);
        // // 计算最大半径
        // int maxRadius = (int) ((mRect.right - mRect.left) / 2);
        // // 控制扩散半径的属性变化
        // PropertyValuesHolder radiusHolder = PropertyValuesHolder.ofInt(String.valueOf(1000), 0,
        // maxRadius);
        // // 控制透明度的属性变化
        // PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0);
        // mValueAnimator = ObjectAnimator.ofPropertyValuesHolder(this, radiusHolder, alphaHolder);
        // mValueAnimator.setStartDelay(mStartDelay);
        
        mHalfHeight = mHeight; // ;(bounds.bottom - bounds.top);
        mHalfWidth = mWidth;// (bounds.right - bounds.left);
        // mDivideSpace = Math.max(mHalfHeight, mHalfWidth) * 3 / 4;
        mFullSpace = (int) Math.sqrt(mHalfWidth * mHalfWidth + mHalfHeight * mHalfHeight);// mWidth;
        // 属性动画
        mValueAnimator = ValueAnimator.ofInt(0, mFullSpace);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (int) animation.getAnimatedValue();
                mRadius = mWidth - mRadius;
                mRadius2 = (int) animation.getAnimatedValue();
                mRadius2 = mWidth - mRadius2 - 20;
//                for (int i = 0; i < 10; i++) {
//
//                    mRadius2 = mWidth - mRadius2 - 20 * i;
//                    Path path2 = new Path();
//                    Paint mPaintFirst = new Paint();
//                    mPaintFirst.setAntiAlias(true);
//                    Shader mShader3 = null;
//                    if (i / 2 == 0) {
//                        LogUtils.i("一号");
//                        mShader3 = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.WHITE, Color.GREEN },
//                                null, Shader.TileMode.CLAMP);
//                    } else if (i / 7 == 0) {
//                        LogUtils.i("二号");
//                        mShader3 = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.YELLOW, Color.BLUE },
//                                null, Shader.TileMode.CLAMP);
//                    } else {
//                        LogUtils.i("三号");
//                        mShader3 = new LinearGradient(0, 0, mWidth, mHeight, new int[] { Color.RED, Color.LTGRAY },
//                                null, Shader.TileMode.CLAMP);
//                    }
//                    list.add(new Bean(mPaintFirst, path2, mRadius2, mShader3));
//                }
                invalidateSelf();
            }
        });
        // 设置动画无限循环
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(5000);
        Interpolator overshootInterpolator = new OvershootInterpolator();
        mValueAnimator.setInterpolator(overshootInterpolator);
        // invalidateSelf();
        start();
    }
    
    class Bean {
        private Paint paint;
        private Path path;
        private int mRadius;
        private Shader mShader;
        
        public Bean(Paint paint0, Path path0, int radius0, Shader mShader3) {
            this.paint = paint0;
            this.path = path0;
            this.mRadius = radius0;
            this.mShader = mShader3;
        }
    }
    
    @Override
    public void start() {
        mValueAnimator.start();
    }
    
    @Override
    public void stop() {
        mValueAnimator.end();
    }
    
    @Override
    public boolean isRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }
    
    public void setHeightAndWeigth(int height, int width) {
        this.mHeight = height;
        this.mWidth = width;
        
        // draw(mCanvas);
        
        // onBoundsChange(rect);
        
    }


}
