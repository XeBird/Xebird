package com.lockon.xebird.db;

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
import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "Checklist")
public class Checklist {
    private static final String TAG = "Checklist";

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
    private String LocationName;
    private double checklistLatitude, checklistLongitude;
    private String Province;
    private String Country = "China";
    private double Distance;
    //TODO: 存储路径数据的类

    private String Protocol;
    private int Number_of_observers;
    private float Duration;
    private boolean All_observations_reported;

    private String Checklist_Comments;

    @Ignore
    private boolean isClose;

    public Checklist() {
    }

    @Ignore
    public Checklist(@NotNull String uid, long startTime, double checklistLatitude, double checklistLongitude) {
        this.uid = uid;
        this.startTime = startTime;
        this.checklistLatitude = checklistLatitude;
        this.checklistLongitude = checklistLongitude;
        Log.i(TAG, "startTime：" + startTime);
    }

    public String getLocation() {
        return LocationName + "              " + Province + "\t" + Country;
    }

    //以下全是getter和setter

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

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
