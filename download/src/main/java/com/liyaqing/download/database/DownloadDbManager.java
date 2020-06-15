package com.liyaqing.download.database;

import android.content.Context;


import com.liyaqing.download.App;
import com.liyaqing.download.DownloadInfoModel;
import com.liyaqing.download.utils.ThreadPool;

import java.io.File;
import java.util.List;

public class DownloadDbManager {
    private static volatile DownloadDbManager instance;
    private Context mContext;

    public static DownloadDbManager get() {
        if (instance == null) {
            synchronized (DownloadDbManager.class) {
                if (instance == null) {
                    instance = new DownloadDbManager();
                }
            }
        }
        return instance;
    }

    private DownloadDbManager() {
        mContext = App.get();
    }

    public synchronized void getAllDownloads(DatabaseUtils.DataCallback<List<DownloadInfoModel>> callback) {
        try {
            ThreadPool.getInstance().execute(() -> {
                List<DownloadInfoModel> allDownloads = DownloadDatabase.getInstance(mContext).downloadInfoDao().getAllDownloads();
                callback.dataSuccess(allDownloads);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void getDownloadInfoById(int id, DatabaseUtils.DataCallback<DownloadInfoModel> callback) {
        try {
            ThreadPool.getInstance().execute(() -> {
                DownloadInfoModel downloadInfo = DownloadDatabase.getInstance(mContext).downloadInfoDao().getDownloadInfoById(id);
                callback.dataSuccess(downloadInfo);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void getDownloadInfoByUrl(String url, DatabaseUtils.DataCallback<DownloadInfoModel> callback) {
        try {
            ThreadPool.getInstance().execute(() -> {
                DownloadInfoModel downloadInfo = DownloadDatabase.getInstance(mContext).downloadInfoDao().getDownloadInfoByUrl(url);
                callback.dataSuccess(downloadInfo);
            });
        } catch (Exception e) {
            callback.dataError(e.fillInStackTrace());
        }
    }

    public synchronized void insertDownload(DownloadInfoModel messageModel) {
        if (messageModel == null) {
            return;
        }
        try {
            getDownloadInfoById(messageModel.getId(), new DatabaseUtils.DataCallback<DownloadInfoModel>() {
                @Override
                public void dataSuccess(DownloadInfoModel downloadInfoModel) {
                    //如果已经有了就不执行插入
                    if (downloadInfoModel == null) {
                        ThreadPool.getInstance().execute(() -> {
                            DownloadDatabase.getInstance(mContext).downloadInfoDao().insertDownload(messageModel);
                        });
                    } else {
                        ThreadPool.getInstance().execute(() -> {
                            DownloadDatabase.getInstance(mContext).downloadInfoDao().updateDownload(messageModel);
                        });
                    }
                }

                @Override
                public void dataError(Throwable throwable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateDownload(DownloadInfoModel model) {
        try {
            ThreadPool.getInstance().execute(() -> {
                DownloadDatabase.getInstance(mContext).downloadInfoDao().updateDownload(model);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void delete(final int id) {
        try {
            getDownloadInfoById(id, new DatabaseUtils.DataCallback<DownloadInfoModel>() {
                @Override
                public void dataSuccess(DownloadInfoModel downloadInfo) {
                    if (downloadInfo == null) {
                        return;
                    }
                    String targetUrl = downloadInfo.getTargetUrl();
                    String fileName = downloadInfo.getFileName();
                    File file = new File(targetUrl + "/" + fileName);
                   delete(file);
                    ThreadPool.getInstance().execute(() -> {
                        DownloadDatabase.getInstance(mContext).downloadInfoDao().delete(id);
                    });
                }

                @Override
                public void dataError(Throwable throwable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteAll() {
        ThreadPool.getInstance().execute(() -> {
            DownloadDatabase.getInstance(mContext).downloadInfoDao().deleteAll();
        });
    }
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }

                for(int i = 0; i < childFiles.length; ++i) {
                    delete(childFiles[i]);
                }

                file.delete();
            }

        }
    }

}
