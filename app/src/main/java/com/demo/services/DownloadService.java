package com.demo.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.demo.R;
import com.demo.crash.CaocConfig;
import com.hjq.toast.ToastUtils;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadBinder downloadBinder = new DownloadBinder();

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downloadBinder;
    }

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            //   getNotificationManager().notify(1, getNotification("Dwonloading..", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;

            stopForeground(true);
            //   getNotificationManager().notify(1, getNotification("Dwonloading..", -1));
            ToastUtils.show("listener下载成功");
        }

        @Override
        public void onFailed() {

            downloadTask = null;
            stopForeground(true);
            //  getNotificationManager().notify(1, getNotification("Dwonloading..", -1));
            ToastUtils.show("listener下载失败");
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            ToastUtils.show("listener下载暂停");
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            ToastUtils.show("listener下载取消");
        }
    };


    class DownloadBinder extends Binder {
        public void startDownload(String url) {
            if (downloadTask == null) {

                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                //  startForeground(1, getNotification("Downloading...", 0));

            }
            ToastUtils.show("DownloadBinder开始下载");
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();

            }
            ToastUtils.show("DownloadBinder暂停下载");
        }

        public void cancleDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                // getNotificationManager().cancel(1);
                stopForeground(true);
                ToastUtils.show("DownloadBinder取消下载");
            }
        }

    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress > 0) {

            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
