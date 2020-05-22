package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Preparing extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 0) {//单纯为了多看一会儿谭雅酱
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Preparing.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 5000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);
        new CopyDB(this,handler).run();

    }


    static class CopyDB  {

        private final int BUFFER_SIZE=400000;//缓冲区大小
        static final String DB_NAME="Birds.db"; //保存的数据库文件名
        static final String PACKAGE_NAME="com.lockon.xebird";//应用的包名
        static final String DB_PATH="/data"
                + Environment.getDataDirectory().getAbsolutePath()+"/"
                +PACKAGE_NAME+"/databases";//在手机里存放数据库的位置
        private Context context;
        private Handler han;
        /* TODO:
         * 输入外部数据库的名字来导入（更新）数据库
         */

        CopyDB(Context context, Handler handler) {
            this.han=handler;
            this.context = context;
        }

        void run() {
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
            han.sendEmptyMessage(0);
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
}
