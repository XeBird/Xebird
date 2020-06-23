package com.lockon.xebird;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;


/**
 * Created by ljk5403 on 2020/06/23.
 * 监听轨迹，并可用于获取位置信息（单例模式）
 * 暂先实现获取位置。 TODO: 监听并记录轨迹
 * 监听位置参考了：https://www.cnblogs.com/tangzh/p/8969898.html
 */

public class Tracker {
    private static final String TAG = "Tracker";

    private static Tracker instance;
    private Context mContext;
    private LocationManager locationManager;
    private String locationProvider = null;


    public LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
        }
    };


    private Tracker(Context context) {
        this.mContext = context.getApplicationContext();

        //地理位置监听
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        assert locationManager != null;
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(i);
        }

        //监视地理位置变化
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.w(TAG, "Insufficient location permission.");
            return;
        }

        // Consider_TODO: 官方文档建议构造一个 LocationRequest 对象来确定 requestLocationUpdates 的参数
        // https://developer.android.com/training/location/request-updates#callback
        assert locationProvider != null;
        locationManager.requestLocationUpdates(locationProvider, 500, 1, locationListener);

        //TODO: 提供计时器；etc.
    }


    public static Tracker getInstance(Context context) {
        if (instance == null) {
            instance = new Tracker(context);
        }
        return instance;
    }

    //以下为getter
    public Location getLatestLocation() {
        //获取Location
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.w(TAG, "Insufficient location permission.");
            return null;
        }
        locationManager.requestLocationUpdates(locationProvider, 500, 1, locationListener);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            Log.i(TAG, "Successfully get location!");
        }
        else{
            Log.e(TAG, "Failed to get location!");
        }
        return location;
    }

    //用1000来代表经纬度的错误返回值
    final double FailedResult = 1000;

    public double getLatestLatitude(){
        Location location = getLatestLocation();
        if (location != null) {
            Log.i(TAG, "Successfully get latitude!");
            return location.getLatitude();
        }
        else{
            Log.e(TAG, "Failed to get latitude!");
            //用1000来代表错误返回值
            return FailedResult;
        }
    }

    public double getLatestLongitude(){
        Location location = getLatestLocation();
        if (location != null) {
            Log.i(TAG, "Successfully get longitude!");
            return location.getLongitude();
        }
        else{
            Log.e(TAG, "Failed to get longitude!");
            //用1000来代表错误返回值
            return FailedResult;
        }
    }




//    /**
//     * 获取经纬度
//     */
//    public String getLngAndLat(OnLocationResultListener onLocationResultListener) {
//        double latitude = 0.0;
//        double longitude = 0.0;
//
//        mOnLocationListener = onLocationResultListener;
//
//
//
//        //获取Location
//        Location location = locationManager.getLastKnownLocation(locationProvider);
//        if (location != null) {
//            //不为空,显示地理位置经纬度
//            if (mOnLocationListener != null) {
//                mOnLocationListener.onLocationResult(location);
//            }
//
//        }
//    }
//
//    public LocationListener locationListener = new LocationListener() {
//
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//            if (mOnLocationListener != null) {
//                mOnLocationListener.OnLocationChange(location);
//            }
//        }
//    };
//
//    public void removeListener() {
//        locationManager.removeUpdates(locationListener);
//    }
//
//    private OnLocationResultListener mOnLocationListener;
//
//    public interface OnLocationResultListener {
//        void onLocationResult(Location location);
//
//        void OnLocationChange(Location location);
//    }
}
