package com.lockon.xebird;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Preparing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);
        new CopyDB(this).run();
        //单纯为了多看一会儿谭雅酱
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Preparing.this, MainActivity.class);
                startActivity(intent);
            }
        },5000);


    }
}
