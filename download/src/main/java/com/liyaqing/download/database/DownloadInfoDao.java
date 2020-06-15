package com.liyaqing.download.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.liyaqing.download.DownloadInfoModel;

import java.util.List;

@Dao
public interface DownloadInfoDao {

    /**
     *
     *
     * @return
     */
    @Query("select * from down_load_info where id = :id")
    DownloadInfoModel getDownloadInfoById(int id);

    @Query("select * from down_load_info where url = :url")
    DownloadInfoModel getDownloadInfoByUrl(String url);


    /**
     *
     *
     * @return
     */
    @Query("select * from down_load_info")
    List<DownloadInfoModel> getAllDownloads();


    /**
     * 插入
     *
     * @param
     */
    @Insert
    void insertDownload(DownloadInfoModel messageModel);


    @Update
    void updateDownload(DownloadInfoModel model);

    /**
     * 删除
     *
     */
    @Query("delete from down_load_info where id =:id")
    void delete(int id);

    @Query("delete from down_load_info")
    void deleteAll();

}

