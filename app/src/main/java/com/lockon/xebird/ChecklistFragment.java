package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.lockon.xebird.db.Checklist;


public class ChecklistFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "ChecklistFragment";

    //计时，主要功能放在参Checklist.TrackerThread
    private static final int msgTime = 1;
    private static final int msgLocation = 2;
    public TextView timerTV;
    public TextView LatitudeTV, LongitudeTV, LocationTV;

    //用1000来代表经纬度错误返回值
    final double FailedResult = 1000;

    public ChecklistFragment() { }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //实例化一个Checklist，数据均存储于其中
        String uid = "20000101235959";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdf= new SimpleDateFormat("yyyyMMddHHmmss");
        mdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        uid = mdf.format(System.currentTimeMillis());
        Log.i(TAG,"UTC:"+uid);
        Checklist checklist = new Checklist(uid,trackerHandler,this.getContext());

        //获取TextView
        timerTV = getView().findViewById(R.id.timer);
        LatitudeTV = getView().findViewById(R.id.Latitude);
        LongitudeTV = getView().findViewById(R.id.Longitude);
        LocationTV = getView().findViewById(R.id.Location);
    }


    @SuppressLint("HandlerLeak")
    private Handler trackerHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgTime:
                    long duration = (long) msg.obj;
                    Log.i(TAG, ""+duration);
                    SimpleDateFormat mdf= new SimpleDateFormat("HH:mm:ss");
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    mdf.setTimeZone(tz);
                    String sysTimeStr = mdf.format(duration);
                    timerTV.setText(sysTimeStr);
                    break;

                case msgLocation:
                    Bundle bundle = (Bundle) msg.obj;
                    double Latitude, Longitude;
                    Latitude = bundle.getDouble("Latitude");
                    Longitude = bundle.getDouble("Longitude");
                    if (Latitude != FailedResult){
                        LatitudeTV.setText(String.valueOf(Latitude));
                    } else{
                        LatitudeTV.setText(R.string.latitude);
                    }
                    if (Longitude != FailedResult){
                        LongitudeTV.setText(String.valueOf(Longitude));
                    } else{
                        LongitudeTV.setText(R.string.longitude);
                    }
                    break;

                default:
                    break;
            }
        }
    };
}