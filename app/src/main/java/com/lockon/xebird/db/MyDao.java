package com.lockon.xebird.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {
    @Query("SELECT * FROM MainDATA")
    List<BirdData> getAll();

    //根据ID查询所有MainDATAIds中的数据
    @Query("SELECT * FROM MainDATA WHERE ID IN (:MainDATAIds)")
    List<BirdData> loadAllByIds(int[] MainDATAIds);

    //同上
    @Query("SELECT * FROM MainDATA WHERE ID = :uid")
    BirdData findByUid(int uid);

    //根据中文名字查询
    @Query("SELECT * FROM MainDATA WHERE NAME_CN like '%' || :nameCN || '%'")
    List<BirdData> findByNameCN(String nameCN);

    //拼音
    @Query("SELECT * FROM MainDATA WHERE NAME_P like '%' || :nameP || '%'")
    List<BirdData> findByNameP(String nameP);

    //俗名
    @Query("SELECT * FROM MainDATA WHERE NAME_POP like '%' || :namePop || '%'")
    List<BirdData> findByNamePop(String namePop);

    //英文
    @Query("SELECT * FROM MainDATA WHERE NAME_EN like '%' || :nameEN || '%'")
    List<BirdData> findByNameEN(String nameEN);

    //拉丁
    @Query("SELECT * FROM MainDATA WHERE NAME_LA like '%' || :nameLA || '%'")
    List<BirdData> findByNameLA(String nameLA);


}
