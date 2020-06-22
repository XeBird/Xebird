package com.lockon.xebird.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "Checklist")
public class Checklist {

    //TODO:设置相关，要加入语言
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.getDefault());

    //unused // private String recordFile="Bird_record";

    //参考 Appendix A (eBird Data Fields).pdf
    @PrimaryKey
    private int uid;
    //时间信息
    private long startTime, endTime;
    //地点信息
    private String LocationName;
    private float Latitude, Longitude;
    private String Province;
    private String Country;
    private float Distance;
    //TODO: 存储路径数据的类

    private String Protocol;
    private int Number_of_observers;
    private float Duration;
    private boolean All_observations_reported;

    private String Checklist_Comments;

    public Checklist() {
    }

    //以下全是getter和setter
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
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

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
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
