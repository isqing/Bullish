package com.liyaqing.download;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.liyaqing.download.database.DatabaseUtils;
import com.liyaqing.download.database.DownloadDbManager;
import com.liyaqing.download.dialog.RzDownloadDialog;
import com.liyaqing.download.utils.AppUtil;
import com.liyaqing.download.utils.MD5Utils;
import com.liyaqing.download.utils.UrlUtils;

import java.io.File;
import java.nio.channels.CompletionHandler;


public class RzDownLoadManager {
    private static volatile RzDownLoadManager instance;
    private Context mContext;
    public RzDownloadDialog dialog =null;
    public static RzDownLoadManager get() {
        if (instance == null) {
            synchronized (RzDownLoadManager.class) {
                if (instance == null) {
                    instance = new RzDownLoadManager();
                }
            }
        }
        return instance;
    }
    private Activity mActivity;

    public  void downloadAndInstallApk(String url, String title, String icon, Activity activity) {
        mActivity=activity;
        String md5Code = MD5Utils.getMD5Code(url);
        String fileName = UrlUtils.guessFileName(url, "", "application/octet-stream", md5Code, "apk");
        if (dialog==null) {
            dialog = new RzDownloadDialog(activity);
            dialog.show();
        }
        dialog.setUrl(url);
        dialog.setIcon(icon);
        dialog.setTitle(title);

         DownloadDbManager.get().getDownloadInfoByUrl(url, new DatabaseUtils.DataCallback<DownloadInfoModel>() {
             @Override
             public void dataSuccess(DownloadInfoModel downloadInfoModel) {
                 if (downloadInfoModel != null){
                     long currentProgress=downloadInfoModel.getProgress();
                     long totalLength=downloadInfoModel.getTotal();
                     int progressValue = (int)(((float) currentProgress / (float) totalLength)*100);

                     dialog.setProgress(progressValue);
                 }
                 download(url,fileName,dialog);
             }

             @Override
             public void dataError(Throwable throwable) {
                    throwable.fillInStackTrace();
             }
         });


    }
    private void download(String url, String fileName, RzDownloadDialog dialog){
        DownloadManager.getInstance(App.get()).download(url, getDiskFileDir(App.get()) + "/"+fileName, new DownloadResponseHandler() {
            @Override
            public void onFinish(File download_file) {
                dialog.dismiss();
                AppUtil.openApk(App.get(), download_file);
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                int progress = (int)(((float) currentBytes / (float) totalBytes)*100);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setProgress(progress);
                    }
                });
            }

            @Override
            public void onFailure(String error_msg) {
            }

            @Override
            public void onPause(DownloadInfoModel info) {

            }

            @Override
            public void onCancle(DownloadInfoModel info) {

            }
        });

    }
    public void pauseDownload(String url){
        DownloadManager.getInstance(App.get()).pause(url);
    }

    /**
     * 获取缓存文件目录
     *
     * @param context
     * @return
     */
    public static String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(null).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }

        return cachePath;
    }

}
