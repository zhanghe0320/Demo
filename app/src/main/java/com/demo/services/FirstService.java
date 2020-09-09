package com.demo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FirstService extends Service {
    private static final String TAG = "FirstService";
    private DownloadBindler mBindler = new DownloadBindler();

    public FirstService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return mBindler;
    }


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: 创建");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: 启动");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: 销毁");
        super.onDestroy();
    }


    public class DownloadBindler extends Binder {
        public void startDown() {
            Log.i(TAG, "startDown: 开始下载任务");

        }

        public int getProgress() {
            Log.i(TAG, "getProgress: 获取下载进度");
            return 0;
        }

        public DownloadBindler() {
            super();
        }

    }
}
