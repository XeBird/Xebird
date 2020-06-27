package com.lockon.xebird.other;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.lockon.xebird.BirdlistFragment;
import com.lockon.xebird.ChecklistFragment;
import com.lockon.xebird.CollectFragment;
import com.lockon.xebird.R;
import com.lockon.xebird.db.BirdData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kotlin.TypeCastException;

import static com.lockon.xebird.R.drawable.no_bitmap;

public class XeBirdHandler {
    public static final int SETBITMAP = 0;
    public static final int SETNULLTEXT = 1;
    public static final int SETLIST = 2;
    public static final int SETNULLBITMAP = 3;
    public static final int INFONAME = 4;
    public static final int INFODETAIL = 5;

    static abstract class BaseHandler extends Handler {
        public WeakReference<Fragment> mFragment;
        public String TAG;
    }


    public static class InfoDetailHandler extends BaseHandler {
        public InfoDetailHandler(CollectFragment f) {
            this.mFragment = new WeakReference<Fragment>(f);
            this.TAG = f.getTAG();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            CollectFragment f = (CollectFragment) mFragment.get();
            assert f != null;
            switch (msg.what) {
                case SETBITMAP:
                    Log.i(TAG, "handleMessage: set bitmap");
                    f.imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                case SETNULLBITMAP:
                    Log.i(TAG, "handleMessage: set NULL bitmap");
                    f.imageView.setImageResource(no_bitmap);
            }
        }
    }

    public static class BirdlistHandler extends BaseHandler {
        public BirdlistHandler(BirdlistFragment fragment) {
            this.mFragment = new WeakReference<Fragment>(fragment);
            this.TAG = fragment.getTAG();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            BirdlistFragment f = (BirdlistFragment) mFragment.get();
            assert f != null;
            switch (msg.what) {
                case SETNULLTEXT:
                    Log.i(TAG, "handleMessage: null get");
                    List<BirdData> bs_null = new ArrayList<>();
                    f.mAdapter.changeList(bs_null);
                    break;
                case SETLIST:
                    if (msg.obj instanceof List<?>) {
                        List<BirdData> bs = (List<BirdData>) msg.obj;
                        for (BirdData b : bs) {
                            Log.i(TAG, "handleMessage: data a ru " + b.getNameCN());
                        }
                        f.mAdapter.changeList(bs);
                    } else {
                        throw new TypeCastException();
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    }

    public static class TrackerHandler extends BaseHandler {
        //计时，主要功能放在参Checklist.TrackerThread
        private static final int msgTime = 1;
        private static final int msgLocation = 2;
        //用1000来代表经纬度错误返回值
        private final double FailedResult = 1000;

        public TrackerHandler(ChecklistFragment fragment) {
            this.mFragment = new WeakReference<Fragment>(fragment);
            this.TAG = fragment.getTAG();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            ChecklistFragment f = (ChecklistFragment) mFragment.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case msgTime:
                    long duration = (long) msg.obj;
                    Log.v(TAG, "Get Message duration: " + duration);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat mdf = new SimpleDateFormat("HH:mm:ss");
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    mdf.setTimeZone(tz);
                    String sysTimeStr = mdf.format(duration);
                    f.timerTV.setText(sysTimeStr);
                    break;

                case msgLocation:
                    Bundle bundle = (Bundle) msg.obj;
                    double Latitude, Longitude;
                    String AddressHint;
                    int ProvinceHint;
                    Latitude = bundle.getDouble("Latitude");
                    Longitude = bundle.getDouble("Longitude");
                    AddressHint = bundle.getString("AddressHint");
                    ProvinceHint = bundle.getInt("ProvinceHint");
                    Log.v(TAG, "Get Message Latitude: " + Latitude);
                    Log.v(TAG, "Get Message Longitude: " + Longitude);
                    Log.v(TAG, "Get Message AddressHint: " + AddressHint);
                    Log.v(TAG, "Get Message ProvinceHint(in Index): " + ProvinceHint);
                    if (Latitude != FailedResult) {
                        f.LatitudeTV.setText("LAT: " + Latitude);
                    } else {
                        f.LatitudeTV.setText(R.string.latitude);
                    }
                    if (Longitude != FailedResult) {
                        f.LongitudeTV.setText("LONG: " + Longitude);
                    } else {
                        f.LongitudeTV.setText(R.string.longitude);
                    }
                    if (!"".equals(AddressHint)) {
                        f.LocationET.setHint(AddressHint);
                    } else {
                        f.LocationET.setHint(R.string.location_hint);
                    }
                    if (ProvinceHint != -1) {
                        f.provinceSpinner.setSelection(ProvinceHint);
                    }
                    break;

                default:
                    break;
            }
        }
    }

//    public static class AddBirdRecordHandler extends BaseHandler {
//        private static final int msgLocation = 2;
//        //用1000来代表经纬度错误返回值
//        private final double FailedResult = 1000;
//
//        public AddBirdRecordHandler(AddBirdRecordFragment fragment) {
//            this.mFragment = new WeakReference<Fragment>(fragment);
//            this.TAG = fragment.getTAG();
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public void handleMessage(Message msg) {
//            AddBirdRecordFragment f = (AddBirdRecordFragment) mFragment.get();
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case msgLocation:
//                    Bundle bundle = (Bundle) msg.obj;
//                    double Latitude, Longitude;
//                    Latitude = bundle.getDouble("Latitude");
//                    Longitude = bundle.getDouble("Longitude");
//                    Log.i(TAG, "Get Message Latitude: " + Latitude);
//                    Log.i(TAG, "Get Message Longitude: " + Longitude);
//                    if (Latitude != FailedResult) {
//                        f.LatitudeTV.setText(String.valueOf(Latitude));
//                    } else {
//                        f.LatitudeTV.setText(R.string.latitude);
//                    }
//                    if (Longitude != FailedResult) {
//                        f.LongitudeTV.setText(String.valueOf(Longitude));
//                    } else {
//                        f.LongitudeTV.setText(R.string.longitude);
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }
}
