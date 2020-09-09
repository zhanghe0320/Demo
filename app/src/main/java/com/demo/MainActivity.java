package com.demo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.demo.fragment.AllFragment;
import com.demo.fragment.MyFragment;
import com.demo.fragment.NewFragment;
import com.demo.fragment.TextFragment;
import com.demo.fragment.WebFragment;
import com.demo.fragment.dao.FragmentBean;
import com.demo.services.DownloadActivity;
import com.demo.services.FirstService;
import com.demo.view.ConstantValue;
import com.demo.view.MainContract;
import com.demo.view.MainPresenter;
import com.demo.view.navigation.NavigationBar;
import com.demo.view.navigation.NavigationBarBean;
import com.demo.view.navigation.NavigationBarRelativeLayout;
import com.demo.view.self.MyTextView;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.IMainView {
    private static final String TAG = "MainActivity";
    @BindView(R.id.framlayout)
    FrameLayout frameLayout;
    @BindView(R.id.main_startservice)
    TextView startService;
    @BindView(R.id.main_stopservice)
    TextView stopService;
    
    @BindView(R.id.main_bindservice)
    TextView bindService;
    @BindView(R.id.main_ubindservice)
    TextView ubindService;
    
    @BindView(R.id.main_todownservice)
    TextView main_todownservice;
    
    @BindView(R.id.mytextview)
    MyTextView mytextview;
    // private MainPresenter iMainPresenter;
    
    private AllFragment mAllFragment;
    private TextFragment mTextFragment;
    private WebFragment mWebFragment;
    private MyFragment mMyFragment;
    private NewFragment mNewFragment;
    
    private Fragment currentFragment;
    private FragmentManager mFragmentManager;
    List list = new ArrayList();
    List postion = new ArrayList();
    private FirstService.DownloadBindler downloadBindler;
    NavigationBarBean navigationBarBean = null;
    
    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }
    
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Looper.getMainLooper();
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        mPresenter.createFragment("创建");
        
        mFragmentManager = getSupportFragmentManager();
        navigationBarBean = new NavigationBarBean(1,"所有");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(-1,"新闻");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(-1,"网页");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"文字");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"我的");
        list.add(navigationBarBean);
        // com.blankj.utilcode.util.ToastUtils.showLong("androidusitk");
        // PermissionUtils.permission(Manifest.permission.LOCATION_HARDWARE);
        // PermissionUtils.launchAppDetailsSettings();
        // PermissionUtils.OnRationaleListener
        
        List list1 = new ArrayList();
        list1.add(PermissionConstants.CAMERA);
        list1.add(PermissionConstants.PHONE);
        list1.add(PermissionConstants.CAMERA);
        
        String permission[] = {};
        // new String[]{PermissionConstants.CONTACTS,PermissionConstants.STORAGE,PermissionConstants.CAMERA,
        // PermissionConstants.LOCATION, PermissionConstants.PHONE, PermissionConstants.MICROPHONE}
        // PermissionUtils.permission(new String[] {
        // PermissionConstants.CONTACTS,
        // PermissionConstants.STORAGE,
        // PermissionConstants.CAMERA,
        // PermissionConstants.LOCATION,
        // PermissionConstants.PHONE,
        // PermissionConstants.MICROPHONE }).rationale(new PermissionUtils.OnRationaleListener() {
        // @Override
        // public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
        // LogUtils.i("请求权限");
        // }
        //
        // }).callback(new PermissionUtils.FullCallback() {
        // @Override
        // public void onGranted(List<String> permissionsGranted) {
        // // updateAboutPermission();
        // }
        //
        // @Override
        // public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
        // if (!permissionsDeniedForever.isEmpty()) {
        // // PermissionHelper.showOpenAppSettingDialog();
        // }
        // LogUtils.d(permissionsDeniedForever, permissionsDenied);
        // }
        // }).theme(new PermissionUtils.ThemeCallback() {
        // @Override
        // public void onActivityCreate(Activity activity) {
        // ScreenUtils.setFullScreen(activity);
        // }
        // }).request();
        
        // PermissionUtils.permission(new String[] {
        // PermissionConstants.CONTACTS,
        // PermissionConstants.STORAGE,
        // PermissionConstants.CAMERA,
        // PermissionConstants.LOCATION,
        // PermissionConstants.PHONE,
        // PermissionConstants.MICROPHONE,
        // PermissionConstants.SMS,
        // PermissionConstants.SENSORS }).rationale(new PermissionUtils.OnRationaleListener() {
        // @Override
        // public void rationale(UtilsTransActivity activity, ShouldRequest shouldRequest) {
        // LogUtils.i("请求权限");
        // }
        // }).callback(new PermissionUtils.FullCallback() {
        //
        // @Override
        // public void onGranted(@NonNull List<String> granted) {
        //
        // }
        //
        // @Override
        // public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
        //
        // }
        // }).theme(new PermissionUtils.ThemeCallback() {
        // @Override
        // public void onActivityCreate(Activity activity) {
        // ScreenUtils.setFullScreen(activity);
        // }
        // }).request();
        
        NavigationBarRelativeLayout.setPresenter((NavigationBar.NavigationPresenter) mPresenter, list);
        initFragment(savedInstanceState);
        ThreadUtils.executeByIo(GetTask("开始"));
        ThreadUtils.cancel();
        mytextview.setText("xiugai wenia");
        mytextview.setTextColor("#FFFFFF");
        
        com.demo.util.ThreadUtils.runningOnUiThread();
    }
    
    // 任务，
    private ThreadUtils.Task task;
    
    public void initFragment(Bundle savedInstanceState) {
        // 判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (mAllFragment == null) {
                mAllFragment = new AllFragment().newInstance("s");
            }
            currentFragment = mAllFragment;
            // ft.addToBackStack(null);
            ft.add(R.id.framlayout, mAllFragment).commit();
        }
        
    }
    
    public static <T> void doTask(final Utils.Task<T> task) {
        // 创建被观察者 Observable，它决定什么时候触发事件以及触发怎样的事件。
        Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                // 线程执行耗时操作
                // task.doOnIOThread();
                // subscriber.onNext(task.getT());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                // 订阅
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        // 主线程更新UI
                        // task.doOnUIThread();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
    
    public void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            
            // 添加渐隐渐现的动画
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                // ft.addToBackStack(from.getTag());
                ft.hide(from).add(R.id.framlayout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                // ft.addToBackStack(from.getTag());
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBindler = (FirstService.DownloadBindler) service;
            downloadBindler.startDown();
            downloadBindler.getProgress();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
    };
    
    @OnClick({
            R.id.main_startservice,
            R.id.main_stopservice,
            R.id.main_bindservice,
            R.id.main_ubindservice,
            R.id.main_todownservice })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.main_stopservice:
            Intent stopIntent = new Intent(this, FirstService.class);
            stopService(stopIntent);
            break;
        
        case R.id.main_startservice:
            Intent startIntent = new Intent(this, FirstService.class);
            startService(startIntent);
            break;
        case R.id.main_bindservice:
            Intent bindIntent = new Intent(this, FirstService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
            // startService(bindIntent);
            break;
        
        case R.id.main_ubindservice:
            // Intent uBindIntent = new Intent(this, FirstService.class);
            Intent ubindIntent = new Intent(this, FirstService.class);
            if (serviceConnection == null) {
                
                return;
            }
            unbindService(serviceConnection);
            // startService(uBindIntent);
            break;
        case R.id.main_todownservice:
            // Intent uBindIntent = new Intent(this, FirstService.class);
            Intent intent = new Intent();
            intent.setClass(this, DownloadActivity.class);
            
            startActivity(intent);
            break;
        
        }
        
    }
    
    @Override
    protected void onResume() {
        NavigationBarRelativeLayout.setPresenter((NavigationBar.NavigationPresenter) mPresenter, list);
        super.onResume();
    }
    
    /**
     * 设置 初始化
     *
     * @param mess
     */
    @Override
    public void CreateFragment(String mess) {
        
    }
    
    @Override
    public void ChangeFragment(String mess) {
        ExecutorService executors = ThreadUtils.getCpuPool();
        DataSourceEnum.DATASOURCE.getConnection();
        switch (mess) {
        case ConstantValue.ALL:
            // ThreadUtils.cancel(task);
            ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask(ConstantValue.ALL));
            if (mAllFragment == null) {
                mAllFragment = new AllFragment().newInstance("s");
            }
            // ThreadUtils.cancel(task);
            switchContent(currentFragment, mAllFragment);
            break;
        case ConstantValue.NEW:
            
            ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask(ConstantValue.NEW));
            if (mNewFragment == null) {
                mNewFragment = new NewFragment().newInstance("s");
            }
            switchContent(currentFragment, mNewFragment);
            
            break;
        case ConstantValue.WEB:
            
            ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask(ConstantValue.WEB));
            if (mWebFragment == null) {
                mWebFragment = new WebFragment().newInstance("s");
            }
            switchContent(currentFragment, mWebFragment);
            
            break;
        case ConstantValue.TEXT:
            
            ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask(ConstantValue.TEXT));
            
            if (mTextFragment == null) {
                mTextFragment = new TextFragment().newInstance("s");
            }
            switchContent(currentFragment, mTextFragment);
            
            break;
        case ConstantValue.My:
            ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask(ConstantValue.My));
            if (mMyFragment == null) {
                mMyFragment = new MyFragment().newInstance("s");
            }
            switchContent(currentFragment, mMyFragment);
            break;
        default:
            // ThreadUtils.executeByCustom(ThreadUtils.getCpuPool(), GetTask("ConstantValue.My"));
            break;
        }
    }
    
    public ThreadUtils.Task GetTask(String mess) {
        
        ThreadUtils.Task task = new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                
                LogUtils.i(mess + "执行任务");
                Map map = new HashMap();
                map.put("key", "0");
                map.put("Object", new FragmentBean("任务", "任务", "任务", "任务", "任务"));
                Thread.sleep(2000);
                return map;
            }
            
            @Override
            public void onSuccess(Object result) {
                Map map = (Map) result;
                // ToastUtils.show(mess);
                if ((map.get("key").toString()).endsWith("0")) {
                    FragmentBean fragmentBean = (FragmentBean) map.get("Object");
                    LogUtils.i(mess + "任务成功" + fragmentBean.toString());
                }
                LogUtils.i(mess + "任务成功");
            }
            
            @Override
            public void onCancel() {
                ToastUtils.show(mess + "取消任务");
                LogUtils.i(mess + "取消任务");
            }
            
            @Override
            public void onFail(Throwable t) {
                LogUtils.i(mess + "任务失败");
                ToastUtils.show(mess + "任务失败");
                
            }
        };
        
        return task;
    }
    
    /**
     * 退出时间
     */
    private long exitTime;
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            // 重写键盘事件分发，onKeyDown方法某些情况下捕获不到，只能在这里写
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        // Snackbar snackbar = Snackbar.make(R.id.mainactivity, "再按一次退出程序", Snackbar.LENGTH_SHORT);
                        // snackbar.getView().setBackgroundResource(R.color.colorPrimary);
                        // snackbar.show();
                        ToastUtils.show("再按一次退出APP界面！");
                        exitTime = System.currentTimeMillis();
                    } else {
                        BaseApplication.exitApp();
                    }
                }
            }, 100);
            
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        Intent stopIntent = new Intent(this, FirstService.class);
        
        stopService(stopIntent);
        mPresenter.detachView();
        // if (iMainPresenter != null) {
        // iMainPresenter.detachView();
        // }
        
        super.onDestroy();
    }
    
    /**
     * 获取上下文
     *
     * @return 上下文
     */
    @Override
    public Context getContext() {
        return null;
    }
}
