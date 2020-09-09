package com.demo.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.demo.R;
import com.hjq.toast.ToastUtils;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadActivity extends AppCompatActivity {
    @BindView(R.id.down_begin)
    TextView downBegin;
    @BindView(R.id.down_start)
    TextView downStart;
    
    @BindView(R.id.down_cancle)
    TextView downCancle;
    @BindView(R.id.down_pause)
    TextView downPause;
    
    private DownloadService.DownloadBinder downloadBindler;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBindler = (DownloadService.DownloadBinder) service;
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }


    @OnClick({ R.id.down_begin, R.id.down_pause, R.id.down_start, R.id.down_cancle })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.down_cancle:
            
            downloadBindler.cancleDownload();
            break;
        
        case R.id.down_pause:
            downloadBindler.pauseDownload();
            
            break;
        case R.id.down_start:
            String url = "https://www.python.org/ftp/python/2.7.15/python-2.7.15.amd64.msi";
            downloadBindler.startDownload(url);
            
            break;
        
        case R.id.down_begin:
            
            break;
        }
        
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
        case 1:
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.show("没有权限");
                finish();
            }
            break;
        default:
            break;
        
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    @Override
    protected void onDestroy() {
        
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
