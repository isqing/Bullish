package com.liyaqing.download.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.liyaqing.download.DownloadInfoModel;


@Database(version = 1, entities = {
        DownloadInfoModel.class,
})
public abstract class DownloadDatabase extends RoomDatabase {
    public static final String DB_NAME = "QK_download.db";
    private static volatile DownloadDatabase INSTANCE;
    public static DownloadDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DownloadDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DownloadDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 消息中心dao
     *
     * @return
     */
    public abstract DownloadInfoDao  downloadInfoDao();
}
