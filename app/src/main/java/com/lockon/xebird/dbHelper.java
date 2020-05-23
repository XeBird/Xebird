package com.lockon.xebird;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mDBase;
    private final String TAG="dbHelper";

    private dbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    dbHelper(Context context){
        this(context, Preparing.CopyDB.DB_NAME,null,1);
    }

    private Cursor findByName(String Name){
        Log.i(TAG, "findByName: get Name "+Name);
        StringBuilder sqlstatement=new StringBuilder("select * from "+ "MainDATA ");
        SQLiteDatabase mDBase=getReadableDatabase();
        if(checkcountname(Name)){
            sqlstatement.append("where NAME_CN like %?% or NAME_POP like %?% COLLATE NOCASE");
            return mDBase.rawQuery(sqlstatement.toString(),new String[]{Name,Name});
        }else{
            sqlstatement.append("where NAME_EN like %?% or NAME_LA like %?% or NAME_P like %?% COLLATE NOCASE");
            return mDBase.rawQuery(sqlstatement.toString(),new String[]{Name,Name,Name});
        }
    }

    String getName_LA(String Name){
        Cursor cursor=findByName(Name);
        String Name_LA=null;
        if(cursor!=null&&cursor.moveToFirst()){
            Name_LA=cursor.getString(cursor.getColumnIndex("NAME_LA"));
        }
        return Name_LA;
    }

    String getData(String Name) {
        Cursor cursor=findByName(Name);
        String visiText=null;
        if(cursor!=null&&cursor.moveToFirst()){
            visiText=cursor.getString(cursor.getColumnIndex("NAME_CN"))
                    +"\t"+cursor.getString(cursor.getColumnIndex("NAME_EN"))
                    +"\n"+cursor.getString(cursor.getColumnIndex("NAME_LA"))
                    +"\n"+cursor.getString(cursor.getColumnIndex("MAIN_INFO"));
        }
        return visiText;
    }

    public boolean checkcountname(String countname)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        return m.find();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
