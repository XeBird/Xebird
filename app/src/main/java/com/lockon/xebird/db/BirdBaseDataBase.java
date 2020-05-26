package com.lockon.xebird.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={BirdData.class},version = 1)
public abstract class BirdBaseDataBase extends RoomDatabase {

    public abstract MyDao myDao();
    private final static String DBNAME="Bird.bd";
    private volatile static BirdBaseDataBase BirdBaseDataBase;

    public static synchronized BirdBaseDataBase getInstance(Context context){
        if(BirdBaseDataBase==null){
            return create(context);
        }else{
            return BirdBaseDataBase;
        }
    }

    private static BirdBaseDataBase create(Context context){
        return Room.databaseBuilder(context,BirdBaseDataBase.class,DBNAME)
                .allowMainThreadQueries()
                .createFromAsset("Bird.db")
                .build();
    }
}
