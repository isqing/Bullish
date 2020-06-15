package com.liyaqing.download;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import java.io.Serializable;

/**
 * Created by yang on 2018/6/21.
 * 下载信息
 */

@Entity(tableName = "down_load_info",
        primaryKeys = {"id"})
public class DownloadInfoModel implements Parcelable, Serializable {
    private int id;
    private String url;//下载路径
    private String targetUrl;//存储路径
    private long total;//总大小
    private long progress;//当前进度
    private String fileName;//名称
    private int downloadState;//下载状态
    private String icon;//图标

    protected DownloadInfoModel(Parcel in) {
        id = in.readInt();
        url = in.readString();
        targetUrl = in.readString();
        total = in.readLong();
        progress = in.readLong();
        fileName = in.readString();
        downloadState = in.readInt();
        icon = in.readString();
    }

    public static final Creator<DownloadInfoModel> CREATOR = new Creator<DownloadInfoModel>() {
        @Override
        public DownloadInfoModel createFromParcel(Parcel in) {
            return new DownloadInfoModel(in);
        }

        @Override
        public DownloadInfoModel[] newArray(int size) {
            return new DownloadInfoModel[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DownloadInfoModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public int getDownloadState() {
        return downloadState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(url);
        parcel.writeString(targetUrl);
        parcel.writeLong(total);
        parcel.writeLong(progress);
        parcel.writeString(fileName);
        parcel.writeInt(downloadState);
        parcel.writeString(icon);
    }
}
