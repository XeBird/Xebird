package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lockon.xebird.db.BirdBaseDataBase;

public class Preparing extends AppCompatActivity {
    private final String TAG="preparing";
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {//单纯为了多看一会儿谭雅酱

                case 0:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Preparing.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 5000);
                    break;
                case 1:
                    Log.i(TAG, "handleMessage: save database to "+msg.obj);
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
        private Context context;
        private Handler han;

        CopyDB(Context context, Handler handler) {
            this.han=handler;
            this.context = context;
        }

        void run() {
            BirdBaseDataBase bd=BirdBaseDataBase.getInstance(context);
            bd.myDao().getAll();


//            Message msg=Message.obtain(han);
//            msg.what=1;
//            msg.obj=totalName;
//            msg.sendToTarget();


            han.sendEmptyMessage(0);
        }
    }
}
