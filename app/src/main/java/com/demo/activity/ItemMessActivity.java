package com.demo.activity;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.drawable.AnimDrawable;
import com.demo.drawable.ProgressDrawable;
import com.demo.drawable.RippleDrawable;
import com.demo.fragment.dao.FragmentBean;
import com.demo.mvp.BasePresenter;
import com.demo.view.navigation.NavigationBar;
import com.demo.view.navigation.NavigationBarBean;
import com.demo.view.navigation.NavigationBarRelativeLayout;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class ItemMessActivity extends BaseActivity implements NavigationBar.NavigationPresenter {
    private static final String TAG = "ItemMessActivity";
    // @BindView(R.id.item_mess_web)
    // WebView mWebView;
    @BindView(R.id.item_mess_prigress)
    RelativeLayout mItemMessPrigress;
    @BindView(R.id.item_mess_prigress2)
    RelativeLayout mItemMessPrigress2;
    
    @BindView(R.id.item_mess_change)
    TextView item_mess_change;
    
    ProgressDrawable progressDrawable = null;
    
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_mess);
        ButterKnife.bind(this);
        FragmentBean fragmentBean = getIntent().getParcelableExtra("fragmentBean");
        Log.i(TAG, "onCreate: " + fragmentBean.toString());
        List list = new ArrayList();
        NavigationBarBean navigationBarBean =null;

        navigationBarBean = new NavigationBarBean(1,"按钮1");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"按钮2");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"按钮3");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"按钮4");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"按钮5");
        list.add(navigationBarBean);
        List postion = new ArrayList();

        NavigationBarRelativeLayout.setPresenter(this, list);
        
        // 声明WebSettings子类
        // WebSettings webSettings = mWebView.getSettings();
        //
        // // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // webSettings.setJavaScriptEnabled(true);
        //
        // // 支持插件
        // // webSettings.setPluginsEnabled(true);
        //
        // // 设置自适应屏幕，两者合用
        // webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
        // webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //
        // // 缩放操作
        // webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
        // webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。若为false，则该WebView不可缩放
        // webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件
        //
        // // 其他细节操作
        // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
        // webSettings.setAllowFileAccess(true); // 设置可以访问文件
        // webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        // webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        // webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式
        // mWebView.loadUrl("https://www.baidu.com");
        // mWebView.canGoBack();
        // mWebView.setOnLongClickListener(new View.OnLongClickListener() {
        // @Override
        // public boolean onLongClick(View v) {
        // ToastUtils.show("长按时间");
        // return false;
        // }
        // });
        
        mItemMessPrigress.setBackground(
                new RippleDrawable(R.color.colorAccent, R.color.colorPrimary, RippleDrawable.MODE_MIDDLE, 200, 100));
        mItemMessPrigress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("点击事件");
            }
        });
        
        AnimDrawable animDrawable = new AnimDrawable();
        int a = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        progressDrawable = new ProgressDrawable(null, a - 110, 70);
        String _ask = "a";
        
        mItemMessPrigress2.setBackground(progressDrawable/* animDrawable */);
        animDrawable.start();
        // progressDrawable.start();
        mItemMessPrigress2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                progressDrawable.stop();
                progressDrawable = null;
                mItemMessPrigress2.setBackground(progressDrawable);
                return false;
            }
        });
        
        mItemMessPrigress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("点击事件");
            }
        });
        
        item_mess_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDrawable.stop();
                progressDrawable = null;
                progressDrawable = new ProgressDrawable(null,
                        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() - 200,
                        70);
                mItemMessPrigress2.setBackground(progressDrawable/* animDrawable */);
            }
        });
        
        init();
    }
    
    private static final float mPointNum = 50f;
    
    private void init() {
        PointF mStartPoint = new PointF(0, 0);
        PointF mEndPoint = new PointF(0, 1200);
        PointF mControlPoint = new PointF(500, 600);
        
        List<PointF> mPointList = new ArrayList<>();
        for (int i = 0; i <= mPointNum; i++) {
            mPointList.add(getBezierPoint(mStartPoint, mEndPoint, mControlPoint, i / mPointNum));
            Log.d("Bezier", "X:" + mPointList.get(i).x + " Y:" + mPointList.get(i).y);
        }
    }
    
    private PointF getBezierPoint(PointF start, PointF end, PointF control, float t) {
        PointF bezierPoint = new PointF();
        bezierPoint.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x;
        bezierPoint.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y;
        return bezierPoint;
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
        // mWebView.destroy();
        // BaseApplication.removeActivity(this);
        if (progressDrawable != null && progressDrawable.isRunning()) {
            progressDrawable.stop();
        }
        
        super.onDestroy();
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
        // && event.getAction() == KeyEvent.ACTION_DOWN
        // && event.getRepeatCount() == 0) {
        //
        // if(mWebView.canGoBack()) {
        // //获取webView的浏览记录
        // WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
        // //这里的判断是为了让页面在有上一个页面的情况下，跳转到上一个html页面，而不是退出当前activity
        // if (mWebBackForwardList.getCurrentIndex() > 0) {
        // String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() -
        // 1).getUrl();
        // if (!historyUrl.equals("https://www.baidu.com")) {
        // mWebView.goBack();
        // return true;
        // }
        // }
        // } else {
        // return true;
        // }
        //
        // }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    public void oneOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        
        ToastUtils.show("第一个按钮被点击");
    }
    
    @Override
    public void twoOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ToastUtils.show("第二个按钮被点击");
        
    }
    
    @Override
    public void threeOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ToastUtils.show("第三个按钮被点击");
    }
    
    @Override
    public void fourOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ToastUtils.show("第四个按钮被点击");
    }
    
    @Override
    public void fiveOnclick(/* NavigationPresenter navigationPresenter, String mess */) {
        ToastUtils.show("第五个按钮被点击");
        
    }
}
