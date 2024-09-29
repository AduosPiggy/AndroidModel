package com.example.androidmodel.tools.downloadUtil;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.androidmodel.base.BaseApp;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  new Thread()->{
 *      //1 对已有文件先进行删除: deleteFile(downloadedPath + filename)
 *      //2 传递参数 开始下载
 *      new DownloadUtil.Builder().setDownloadFileName(filename)
 *                 .setDownloadUrl(url)
 *                 .setDownloadedPath(downloadedPath)
 *                 .setDownloadListener(this)
 *                 .build()
 *                 .download();
 *      //3 重写downloadSuccess downloadFail
 *  }
 */

public class Kfflso_DownloadUtil {

    DownloadManager downloadManager;
    long nowDownId;
    private String downloadFileName;
    private String downloadUrl;
    private String downloadedPath;
    private DownloadListener downloadListener;
    // 定时器
    ScheduledExecutorService mScheduledExecutorService;

    public static class Builder {
        private String downloadFileName;
        private String downloadUrl;
        private String downloadedPath;
        private DownloadListener downloadListener;

        public Builder setDownloadFileName(String fileName) {
            this.downloadFileName = fileName;
            return this;
        }

        public Builder setDownloadUrl(String url) {
            this.downloadUrl = url;
            return this;
        }

        public Builder setDownloadedPath(String path) {
            this.downloadedPath = path;
            return this;
        }

        public Builder setDownloadListener(DownloadListener listener) {
            this.downloadListener = listener;
            return this;
        }

        public Kfflso_DownloadUtil build() {
            return new Kfflso_DownloadUtil(this);
        }
    }

    public Kfflso_DownloadUtil(Builder builder) {
        downloadManager = (DownloadManager) BaseApp.application.getSystemService(Context.DOWNLOAD_SERVICE);
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.downloadFileName = builder.downloadFileName;
        this.downloadUrl = builder.downloadUrl;
        this.downloadedPath = builder.downloadedPath;
        this.downloadListener = builder.downloadListener;
    }

    public void download() {
        // 对下载路径进行非空判断
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        //开始下载
        Uri resource = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(resource);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//        request.setAllowedOverRoaming(false);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);
        //在通知栏中显
        request.setShowRunningNotification(false);
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //sdcard的目录下的download文件夹
        request.setDestinationInExternalPublicDir(downloadedPath, downloadFileName);
        request.setTitle(downloadFileName);
        nowDownId = downloadManager.enqueue(request);
        BaseApp.application.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        // 启动定时检测任务
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                queryDownloadStatus();
            }
        }, Kfflso_DownloadUtilCfg.getInstance().getDownloadQueryTime(), Kfflso_DownloadUtilCfg.getInstance().getDownloadQueryTime(), TimeUnit.MILLISECONDS);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            queryDownloadStatus();
        }
    };

    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(nowDownId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            int columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = c.getInt(columnReason);
            String downfilenmae = null;
            String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            if (downloadFileLocalUri != null) {
                File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
                downfilenmae = mFile.getName();
            }
            Log.d("DownloadStatus", "status->" + status + "...reason->" + reason +"...downfilenmae->"+downfilenmae);
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.d("DownloadStatus", "STATUS_PAUSED");
                case DownloadManager.STATUS_FAILED:
                    Log.d("DownloadStatus", "STATUS_FAILED");
                    if (downloadListener != null) {
                        downloadListener.downloadFail(downfilenmae);
                    }
                    closeDownload();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d("DownloadStatus", "STATUS_SUCCESSFUL");
                    if (downloadListener != null) {
                        downloadListener.downloadSuccess(downfilenmae);
                    }
                    closeDownload();
                    break;
            }
        }
    }

    private void closeDownload() {
        // 取消下载完成广播
        unregisteReceiver();
        // 删除此次下载任务
        downloadManager.remove(nowDownId);
        // 关闭定时器
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdown();
        }
    }

    private void unregisteReceiver() {
        BaseApp.application.unregisterReceiver(receiver);
    }

    public interface DownloadListener {
        void downloadSuccess(String filename);

        void downloadFail(String filename);
    }

}

