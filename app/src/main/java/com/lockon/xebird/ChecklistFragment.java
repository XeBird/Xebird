package com.lockon.xebird;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.db.Checklist;
import com.lockon.xebird.other.XeBirdHandler;

public class ChecklistFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "ChecklistFragment";

    //计时，主要功能放在参Checklist.TrackerThread
    private static final int msgTime = 1;
    private static final int msgLocation = 2;
    public TextView timerTV;
    public TextView LatitudeTV, LongitudeTV, LocationTV;

    public static XeBirdHandler.TrackerHandler trackerHandler;



    public ChecklistFragment() {
    }

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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //请求地理位置权限
        int hasACCESS_FINE_LOCATIONPermission =
                ContextCompat.checkSelfPermission(this.requireActivity().getApplication(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasACCESS_COARSE_LOCATIONPermission =
                ContextCompat.checkSelfPermission(this.requireActivity().getApplication(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((hasACCESS_FINE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED) ||
                (hasACCESS_COARSE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }, 1);
        }

        //获取TextView
        timerTV = view.findViewById(R.id.timer);
        LatitudeTV = view.findViewById(R.id.Latitude);
        LongitudeTV = view.findViewById(R.id.Longitude);
        LocationTV = view.findViewById(R.id.Location);

        //实例化一个Checklist，数据均存储于其中
        String uid = "20000101235959";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdf = new SimpleDateFormat("yyyyMMddHHmmss");
        mdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        uid = mdf.format(System.currentTimeMillis());
        Log.i(TAG, "UTC:" + uid);
        trackerHandler = new XeBirdHandler.TrackerHandler(this);
        final Checklist checklist = new Checklist(uid, trackerHandler, this.getContext());

        final Bundle bundle = new Bundle();
        bundle.putString("checklistId", checklist.getUid());
        view.findViewById(R.id.add_birdrecord_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BirdRecordDataBase db = BirdRecordDataBase.getInstance(getContext());
                db.myDao().insertToChecklist(checklist);
                Navigation.findNavController(view)
                        .navigate(R.id.action_checklistFragment_to_birdlistFragment, bundle);
            }
        });
    }
}