package com.lockon.xebird.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.lockon.xebird.other.Tracker;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "Checklist")
public class Checklist {
    private static final String TAG = "Checklist";

    //TODO:设置相关，要加入语言
    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.getDefault());

    //unused // private String recordFile="Bird_record";

    //参考 Appendix A (eBird Data Fields).pdf
    @PrimaryKey
    @NonNull
    private String uid;

    {
        uid = "20000101000000";
    }

    //时间信息
    private long startTime, endTime;

    //地点信息
    @Ignore
    private Tracker tracker;
    //用1000来代表经纬度错误返回值
    @Ignore
    final double FailedResult = 1000;
    @Ignore
    private Handler trackerHandler;
    @Ignore
    private static Thread trackerThread = null;
    private String LocationName;
    private double checklistLatitude, checklistLongitude;
    private String Province;
    private String Country;
    private double Distance;
    //TODO: 存储路径数据的类

    private String Protocol;
    private int Number_of_observers;
    private float Duration;
    private boolean All_observations_reported;

    private String Checklist_Comments;

    public Checklist(){}

    @Ignore
    public Checklist(@NotNull String uid, Handler trackerHandler, Context context) {
        this.uid = uid;
        this.trackerHandler = trackerHandler;
        tracker = Tracker.getInstance(context.getApplicationContext());
        startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime：" + startTime);
        if (trackerThread == null) {
            trackerThread = new TrackerThread();
            trackerThread.start();
        }
    }

    //计时，参考了 https://www.xp.cn/b.php/86888.html
    @Ignore
    private static final int msgTime = 1;
    @Ignore
    private static final int msgLocation = 2;

    public String getTime() {
        return startTime + "\t" + endTime;
    }

    public String getLocation() {
        return LocationName + "\n" + Province + "\t" + Country;
    }

    public class TrackerThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);

                    //获取时间间隔
                    long sysTime = System.currentTimeMillis();
                    Message msg1 = new Message();
                    msg1.what = msgTime;
                    msg1.obj = sysTime - startTime;
                    Log.i(TAG, "sysTime：" + sysTime + " startTime：" + startTime);
                    trackerHandler.sendMessage(msg1);

                    //获取地理位置
                    Bundle bundle =  new Bundle();
                    double Latitude = FailedResult;
                    double Longitude = FailedResult;
                    Latitude = tracker.getLatestLatitude();
                    Longitude = tracker.getLatestLongitude();
                    bundle.putDouble("Latitude", Latitude);
                    bundle.putDouble("Longitude", Longitude);
                    Message msg2 = new Message();
                    msg2.what = msgLocation;
                    msg2.obj = bundle;
                    trackerHandler.sendMessage(msg2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //以下全是getter和setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getChecklistLatitude() {
        return checklistLatitude;
    }

    public void setChecklistLatitude(double checklistLatitude) {
        this.checklistLatitude = checklistLatitude;
    }

    public double getChecklistLongitude() {
        return checklistLongitude;
    }

    public void setChecklistLongitude(double checklistLongitude) {
        this.checklistLongitude = checklistLongitude;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public int getNumber_of_observers() {
        return Number_of_observers;
    }

    public void setNumber_of_observers(int number_of_observers) {
        Number_of_observers = number_of_observers;
    }

    public float getDuration() {
        return Duration;
    }

    public void setDuration(float duration) {
        Duration = duration;
    }

    public boolean isAll_observations_reported() {
        return All_observations_reported;
    }

    public void setAll_observations_reported(boolean all_observations_reported) {
        All_observations_reported = all_observations_reported;
    }

    public String getChecklist_Comments() {
        return Checklist_Comments;
    }

    public void setChecklist_Comments(String checklist_Comments) {
        Checklist_Comments = checklist_Comments;
    }
}
