package com.lockon.xebird;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class checklist {

    //unused // private String recordFile="Bird_record";

    //参考 Appendix A (eBird Data Fields).pdf
    //时间信息
    private SimpleDateFormat startTime, stoptime; //参考 https://blog.csdn.net/gubaohua/article/details/575488
    //地点信息
    private String LocationName;
    private float Latitude, Longitude;
    private String Province;
    private String Country;
    private float Distance;
    //TODO: 存储路径数据的类

    private String Protocol;
    private Integer Number_of_observers;
    private float Duration;
    private boolean All_observations_reported;

    private String Checklist_Comments;

    public checklist(){
        SimpleDateFormat startTime =new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

    }

    public void getLocation(){
        if (GPSUtils.getInstance(this).isLocationProviderEnabled) {
            Location location = GPSUtils.getInstance(this).getLocation();
            if(location != null)
            {
            }
            //showLocationWithToast()
        } else {
            //requestLocation()
        }
    }



    public void addRecord(){}
    public void rewriteRecord(){}
    public void deleteRecord(){}
    public void searchRecord(){}



}
