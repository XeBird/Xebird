package com.lockon.xebird.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={BirdData.class},version = 1)
public abstract class MyDataBase extends RoomDatabase {

    public abstract MyDao myDao();
}