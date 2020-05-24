package com.lockon.xebird.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {
    @Query("SELECT * FROM MainDATA")
    List<BirdData> getAll();

    //根据条件查询，方法参数和注解的sql语句参数一一对应
    @Query("SELECT * FROM MainDATA WHERE ID IN (:MainDATAIds)")
    List<BirdData> loadAllByIds(int[] MainDATAIds);

    //同上
    @Query("SELECT * FROM MainDATA WHERE ID = :uid")
    BirdData findByUid(int uid);
}
