package com.lockon.xebird;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyDB  {

    private final int BUFFER_SIZE=400000;//缓冲区大小
    public static final String DB_NAME="Birds.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME="com.lockon.xebird";//应用的包名
    public static final String DB_PATH="/data"
            + Environment.getDataDirectory().getAbsolutePath()+"/"
            +PACKAGE_NAME+"/databases";//在手机里存放数据库的位置
    private Context context;
    /* TODO:
    * 输入外部数据库的名字来导入（更新）数据库
    */

    public CopyDB(Context context) {
        this.context = context;
    }

    public void run() {
        File DBdir=new File(DB_PATH);
        String totalName=DB_PATH+"/"+DB_NAME;
        File DB =new File(totalName);
        if(!DBdir.exists()) {
            DBdir.mkdirs();
            copyDatabase(DB);
        }else{
            if(!DB.exists()){
                copyDatabase(DB);
            }
        }
    }

    private void copyDatabase (File DB){
        try {
            InputStream DBin=this.context.getAssets().open("Bird.db");
            FileOutputStream DBout=new FileOutputStream(DB);
            byte[] buffer=new byte[BUFFER_SIZE];
            int count=0;
            while((count=DBin.read(buffer))>0){
                DBout.write(buffer);
                DBout.flush();
            }
            DBin.close();
            DBout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
