package com.lockon.xebird;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mDBase;

    private dbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    dbHelper(Context context){
        this(context, Preparing.CopyDB.DB_NAME,null,1);
    }

    private Cursor findByName(String Name, String Table){
        String sqlstatement="select * from "+Table+" where NAME_CN = ?";
        SQLiteDatabase mDBase=getReadableDatabase();
        return mDBase.rawQuery(sqlstatement,new String[]{Name});
    }

    String getName_LA(String Name){
        Cursor cursor=findByName(Name,"MainDATA");
        String Name_LA=null;
        if(cursor!=null&&cursor.moveToFirst()){
            Name_LA=cursor.getString(cursor.getColumnIndex("NAME_LA"));
        }
        return Name_LA;
    }

    String getData(String Name) {
        Cursor cursor=findByName(Name,"MainDATA");
        String visiText=null;
        if(cursor!=null&&cursor.moveToFirst()){
            visiText=cursor.getString(cursor.getColumnIndex("NAME_CN"))
                    +"\t"+cursor.getString(cursor.getColumnIndex("NAME_EN"))
                    +"\n"+cursor.getString(cursor.getColumnIndex("NAME_LA"))
                    +"\n"+cursor.getString(cursor.getColumnIndex("MAIN_INFO"));
        }
        return visiText;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
