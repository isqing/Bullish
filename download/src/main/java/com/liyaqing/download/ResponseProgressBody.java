package com.liyaqing.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import com.liyaqing.download.database.DatabaseUtils;
import com.liyaqing.download.database.DownloadDbManager;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * 重写responsebody 设置下载进度监听
 * Created by yang on 18/6/29.
 */
public class ResponseProgressBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private DownloadResponseHandler mDownloadResponseHandler;
    private BufferedSource bufferedSource;
    private DownloadInfoModel info;
    private long progress;//开始前已下载进度
    private Context mContext;
    private List<DownloadInfoModel> downloadInfos;
    private Handler handler;

    public ResponseProgressBody(Context context, ResponseBody responseBody, DownloadResponseHandler downloadResponseHandler, DownloadInfoModel info) {
        this.mResponseBody = responseBody;
        this.mDownloadResponseHandler = downloadResponseHandler;
        this.info = info;
        this.mContext = context;
        progress = info.getProgress();
        handler=new Handler(Looper.getMainLooper());
//        downloadInfos = SpTool.getMap(mContext, DownloadManager.DOWNLOAD_MAPS);
        DownloadDbManager.get().getAllDownloads(new DatabaseUtils.DataCallback<List<DownloadInfoModel>>() {
            @Override
            public void dataSuccess(List<DownloadInfoModel> downloadInfoModels) {
                downloadInfos=downloadInfoModels;
                if (info.getTotal() <= 0) {
                    info.setTotal(mResponseBody.contentLength());
                    if (downloadInfos != null) {
//                downloadInfos.put(info.getUrl(), info);
                        DownloadDbManager.get().insertDownload(info);
//                SpTool.putMap(mContext, DownloadManager.DOWNLOAD_MAPS, downloadInfos);
                    }
                }

            }

            @Override
            public void dataError(Throwable throwable) {

            }
        });

    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (mDownloadResponseHandler != null) {
                    info.setProgress(totalBytesRead + progress);
//                    mDownloadResponseHandler.onProgress(totalBytesRead+(info!=null?info.getProgress():0), info!=null?info.getProgress()+mResponseBody.contentLength():mResponseBody.contentLength());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadResponseHandler.onProgress(info.getProgress(), info.getTotal());
                        }
                    });
                    info.setDownloadState(DownloadManager.DOWNLOAD_STATE_DOWNLOADING);

//                    if (downloadInfos != null) {
//                        downloadInfos.put(info.getUrl(), info);
//                        SpTool.putMap(mContext, DownloadManager.DOWNLOAD_MAPS, downloadInfos);
//                    }
                    DownloadDbManager.get().updateDownload(info);
                }
                return bytesRead;
            }
        };
    }
}
