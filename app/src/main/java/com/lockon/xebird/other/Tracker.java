package com.lockon.xebird.other;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.lockon.xebird.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


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
    private long lastUpdateAddressJSON = 0;
    private JSONObject addressJSON = null;
    private int ADDRESS_MAX_UPDATE_INTERVAL = 300000;


    private Tracker(Context context) {
        this.mContext = context;

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
//            Intent i = new Intent();
//            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            mContext.startActivity(i);
            //TODO: 申请权限/打开GPS？
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
        }

        // TODO -Consider: 官方文档建议构造一个 LocationRequest 对象来确定 requestLocationUpdates 的参数
        // https://developer.android.com/training/location/request-updates#callback
        assert locationProvider != null;
        locationManager.requestLocationUpdates(locationProvider, 10000, 1, locationListener);
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
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            Log.v(TAG, "Successfully get location!");
        } else {
            Log.w(TAG, "Failed to get location!");
        }
        return location;
    }

    public void stopTracker() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
        if (locationListener != null) {
            locationListener = null;
        }
        instance = null;
    }

    //用1000来代表经纬度的错误返回值
    final double FailedResult = 1000;

    public double getLatestLatitude() {
        Log.i(TAG, "getLatestLatitude");
        Location location = getLatestLocation();
        if (location != null) {
            Log.v(TAG, "Successfully get latitude!");
            return formatDouble(location.getLatitude());
        } else {
            Log.w(TAG, "Failed to get latitude!");
            //用1000来代表错误返回值
            return FailedResult;
        }
    }

    public double getLatestLongitude() {
        Location location = getLatestLocation();
        if (location != null) {
            Log.v(TAG, "Successfully get longitude!");
            return formatDouble(location.getLongitude());
        } else {
            Log.w(TAG, "Failed to get longitude!");
            //用1000来代表错误返回值
            return FailedResult;
        }
    }

    public JSONObject getLatestAddressJSON() {
        Log.i(TAG, "getLatestAddressJSON");
        updateAddressJSONNow();
        return addressJSON;
    }

    public String getLatestAddress() throws JSONException {
        JSONObject jsonObj = getLatestAddressJSON();
        if (jsonObj != null) {
            //Log.v(TAG, jsonObj.getJSONObject("regeocode").getString("formatted_address"));
            return jsonObj.getJSONObject("regeocode").getString("formatted_address");
        } else {
            return "";
        }
    }

    public int getLatestProvince() throws JSONException {
        JSONObject jsonObj = getLatestAddressJSON();
        String provinceStr;
        if (jsonObj != null) {
            provinceStr =  jsonObj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("province");
        } else {
            provinceStr =  "";
        }
        return transformStringToIndexForProvince(provinceStr);
    }

    public JSONObject getCachedAddressJSON() {
        Log.i(TAG, "getLatestAddressJSON");
        updateAddressJSON();
        return addressJSON;
    }

    public String getCachedAddress() throws JSONException {
        JSONObject jsonObj = getCachedAddressJSON();
        if (jsonObj != null) {
            //Log.v(TAG, jsonObj.getJSONObject("regeocode").getString("formatted_address"));
            return jsonObj.getJSONObject("regeocode").getString("formatted_address");
        } else {
            return "";
        }
    }

    public int getCachedProvince() throws JSONException {
        JSONObject jsonObj = getCachedAddressJSON();
        String provinceStr;
        if (jsonObj != null) {
            provinceStr =  jsonObj.getJSONObject("regeocode").getJSONObject("addressComponent").getString("province");
        } else {
            provinceStr =  "";
        }
        return transformStringToIndexForProvince(provinceStr);
    }

    public void updateAddressJSON() {
        if ((System.currentTimeMillis() - lastUpdateAddressJSON) > ADDRESS_MAX_UPDATE_INTERVAL) {
            Log.i(TAG, "updateAddressJSON!");
            updateAddressJSONNow();
        }
    }

    public void updateAddressJSONNow() {
        Log.i(TAG, "updateAddressJSONNow!");

        final double latitude = this.getLatestLatitude();
        final double longitude = this.getLatestLongitude();
        if (longitude != FailedResult && latitude != FailedResult) {
            Callable<JSONObject> callable = new Callable<JSONObject>() {
                @Override
                public JSONObject call() throws Exception {

                    JSONObject jsonObj = null;
                    StringBuilder urlStr = new StringBuilder("https://restapi.amap.com/v3/geocode/regeo?");
                    String mapKey = "e31bae5591a87260b6cc9a0cce705d12";
                    urlStr.append("output=json");

                    String location = longitude + "," + latitude;
                    urlStr.append("&location=").append(location);
                    urlStr.append("&key=").append(mapKey);

                    URL url = new URL(urlStr.toString());
                    //Log.v(TAG, "url: "+url.toString());

                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");// 设置请求方法为GET
                        conn.setReadTimeout(5000);// 设置读取超时为5秒
                        conn.setConnectTimeout(5000);// 设置连接网络超时为5秒
                        conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容
                        conn.setDoInput(true);

                        //返回输入流
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        //读取地理位置，判断是否成功
                        jsonObj = new JSONObject(response.toString());
                        if (jsonObj.getInt("status") != 1) {
                            Log.w(TAG, "Amap location service failed! Info: "
                                    + jsonObj.getString("info"));
                            jsonObj = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            conn.disconnect();// 关闭连接
                        }
                    }
                    return jsonObj;
                }
            };

            FutureTask<JSONObject> futureTask = new FutureTask<JSONObject>(callable);

            Thread thread = new Thread(futureTask);
            thread.start();

            try {
                addressJSON = futureTask.get();
                if (addressJSON != null) {
                    lastUpdateAddressJSON = System.currentTimeMillis();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Failed to get latitude and longitude, so no location result!");
        }
    }

    public int transformStringToIndexForProvince (String provinceStr){
        String[] provinceList = mContext.getResources().getStringArray(R.array.province);
        int index = -1;
        for (int i = 0; i < provinceList.length; i++) {
            if (provinceList[i].equals(provinceStr)) {
                index = i;
                break;
            }
        }
        return index;
    }

    //保留5位小数
    public final double formatDouble(double d) {
        return ((double) ((int) (d * 1000000))) / 1000000;
    }

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
}