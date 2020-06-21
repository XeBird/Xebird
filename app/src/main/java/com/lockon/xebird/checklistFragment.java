package com.lockon.xebird;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class checklistFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "Checklist";

    //计时，参考了 https://www.xp.cn/b.php/86888.html
    private static final int msgKey1 = 1;
    public TextView timer;
    private long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    final public String getTAG() {
        return TAG;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //计时，timer;
        //下方的TimeThread 和 timeHandler 也是用于计时
        timer = (TextView) getView().findViewById(R.id.timer);
        new TimeThread().start();
        startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime："+startTime);
    }

    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    timeHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    private Handler timeHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis()-startTime;
                    Log.i(TAG, "sysTime："+sysTime);
                    SimpleDateFormat mdf= new SimpleDateFormat("HH:mm:ss");
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    mdf.setTimeZone(tz);
                    String sysTimeStr = mdf.format(sysTime);
                    timer.setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };
}
