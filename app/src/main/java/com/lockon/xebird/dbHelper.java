package com.lockon.xebird;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mDBase;

    public dbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public dbHelper(Context context){
        this(context, CopyDB.DB_NAME,null,1);
    }

    Cursor findByName(String Name, String Table){
        String sqlstatement="select * from "+Table+" where NAME_CN = ?";
        SQLiteDatabase mDBase=getReadableDatabase();
        return mDBase.rawQuery(sqlstatement,new String[]{Name});
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
