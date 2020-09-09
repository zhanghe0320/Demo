package com.demo.view.status;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.demo.BaseApplication;
import com.demo.R;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * 标题栏
 */
public class StatusBarRelativeLayout extends RelativeLayout implements View.OnClickListener, StatusBarContract.IStatusBarRelativeLayout, CustomAdapt {
    @BindView(R.id.status_back)
    TextView mStatusBack;
    @BindView(R.id.status_set)
    TextView mStatusSet;
    @BindView(R.id.status_title)
    TextView mStatusTitle;


    StatusBarContract.IStatusBarPresenter StatusationBarPresenter;

    public StatusBarRelativeLayout(Context context) {
        super(context);
    }

    public StatusBarRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public StatusBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatusBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initData(AttributeSet attrs) {

    }


    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.relativelayout_statusbar, this);
        ButterKnife.bind(this);
        mStatusBack.setOnClickListener(this::onClick);
        mStatusSet.setOnClickListener(this::onClick);
        mStatusTitle.setText("主要标题栏");
        if (((Activity) context).getComponentName().getClassName().toLowerCase().contains("mainactivity")) {
            mStatusBack.setText("退出");
        } else {
            mStatusBack.setText("返回");
        }

        mStatusSet.setText("设置");

        StatusationBarPresenter = new StatusationBarPresenter(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_back:
                StatusationBarPresenter.Back((Activity) getContext());
                break;
            case R.id.status_set:
                StatusationBarPresenter.MainSet((Activity) getContext());
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void Back(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }

        if (activity.getComponentName().getClassName().toLowerCase().contains("mainactivity")) {
            new AlertDialog.Builder(activity)
                    .setTitle("确认退出吗？")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage("即将退出app")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BaseApplication.removeActivity(activity);
                                    //ActivityUtils.finishActivity(activity);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create()
                    .show();

        } else {
            //ActivityUtils.finishActivity(activity);
            BaseApplication.removeActivity(activity);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void MainSet(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        ToastUtils.show("主页面点击事件");
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
