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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Ignore;

import com.lockon.xebird.db.BirdRecord;
import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.db.Checklist;
import com.lockon.xebird.other.Tracker;
import com.lockon.xebird.other.XeBirdHandler;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class ChecklistFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "ChecklistFragment";

    //计时，主要功能放在参Checklist.TrackerThread
    private static final int msgTime = 1;
    private static final int msgLocation = 2;
    public TextView timerTV, startAtTv;
    public TextView LatitudeTV, LongitudeTV;
    public EditText observersET, LocationET, commentsET;
    public Spinner protocolSpinner, provinceSpinner;
    public String provinceStr = "", protocolStr = "";
    public CheckBox allObservationsReportedCheckBox;
    public boolean allObservationsReported;

    public String uid;
    public long startTime;
    public Checklist checklist;

    public Thread trackerThread = null;
    public XeBirdHandler.TrackerHandler trackerHandler;
    public Handler loopHandler;
    public Tracker tracker;

    public static BirdRecordDataBase db;


    public ChecklistFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tracker for auto fill location
        tracker = Tracker.getInstance(requireContext());
        trackerHandler = new XeBirdHandler.TrackerHandler(this);

        //loop trackerThread for continuous update
        loopHandler = new Handler();
        trackerThread = new TrackerThread();
        trackerThread.start();

        //实例化一个Checklist，数据均存储于其中
        startTime = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdf = new SimpleDateFormat("yyyyMMddHHmmss");
        mdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        uid = mdf.format(startTime);
        Log.v(TAG, "UTC:" + uid);

        checklist = new Checklist(uid, startTime, tracker.getLatestLatitude(), tracker.getLatestLongitude());
        db = BirdRecordDataBase.getInstance(getContext());
        db.myDao().insertToChecklist(checklist);

        Log.i(TAG, "onCreate!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView!");
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    final public String getTAG() {
        return TAG;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView!");

//        //请求地理位置权限
//        int hasACCESS_FINE_LOCATIONPermission =
//                ContextCompat.checkSelfPermission(this.requireActivity().getApplication(),
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//        int hasACCESS_COARSE_LOCATIONPermission =
//                ContextCompat.checkSelfPermission(this.requireActivity().getApplication(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION);
//        if ((hasACCESS_FINE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED) ||
//                (hasACCESS_COARSE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED)) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            }, 1);
//        }


        //获取TextView
        timerTV = view.findViewById(R.id.timer);
        startAtTv = view.findViewById(R.id.start_time);
        LatitudeTV = view.findViewById(R.id.Latitude);
        LongitudeTV = view.findViewById(R.id.Longitude);

        observersET = view.findViewById(R.id.observers);
        LocationET = view.findViewById(R.id.Location);
        commentsET = view.findViewById(R.id.Checklist_Comments);

        protocolSpinner = view.findViewById(R.id.protocol);
        provinceSpinner = view.findViewById(R.id.province);

        allObservationsReportedCheckBox = view.findViewById(R.id.all_observations_reported);

        //start_time
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdf2 = new SimpleDateFormat("HH:mm:ss");
        mdf2.setTimeZone(TimeZone.getDefault());
        startAtTv.setText(mdf2.format(startTime));

        //protocolSpinner
        ArrayAdapter protocolAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.protocol, android.R.layout.simple_spinner_item);
        protocolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        protocolSpinner.setAdapter(protocolAdapter);
        protocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                protocolStr = getResources().getStringArray(R.array.protocol)[position];
                Log.v(TAG, protocolStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                provinceStr = "";
            }
        });


        //provinceSpinner
        ArrayAdapter provinceAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.province, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceStr = getResources().getStringArray(R.array.province)[position];
                Log.v(TAG, provinceStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                provinceStr = "";
            }
        });

        //allObservationsReportedCheckBox
        allObservationsReportedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allObservationsReported = isChecked;
            }
        });

        //autofill_button
        view.findViewById(R.id.auto_fill_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address, province;
                try {
                    address = tracker.getLatestAddress();
                    LocationET.setText(address);

                    province = tracker.getLatestProvince();
                    String[] provinceList = getResources().getStringArray(R.array.province);
                    int index = -1;
                    for (int i = 0; i < provinceList.length; i++) {
                        if (provinceList[i].equals(province)) {
                            index = i;
                            break;
                        }
                    }
                    provinceSpinner.setSelection(index);
                } catch (MalformedURLException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //add_button
        final Bundle bundle = new Bundle();
        bundle.putString("checklistId", checklist.getUid());
        view.findViewById(R.id.add_birdrecord_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_checklistFragment_to_birdlistFragment, bundle);
            }
        });

        //submit_button
        view.findViewById(R.id.submit_checklist_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(observersET.getText().toString())) {
                    int observers = Integer.parseInt(observersET.getText().toString());
                    if (observers > 0) {
                        if (!"".equals(protocolStr)) {
                            if (!"".equals(provinceStr)) {
                                List<BirdRecord> birdRecordList =
                                        db.myDao().getAllByCid(checklist.getUid());
                                if (null != birdRecordList && !birdRecordList.isEmpty()) {

                                    //submit the checklist
                                    checklist.setEndTime(System.currentTimeMillis());
                                    checklist.setNumber_of_observers(observers);
                                    checklist.setLocationName(LocationET.getText().toString());
                                    checklist.setChecklist_Comments(commentsET.getText().toString());
                                    checklist.setProtocol(protocolStr);
                                    checklist.setProvince(provinceStr);
                                    checklist.setAll_observations_reported(allObservationsReported);
                                    db.myDao().updateInChecklist(checklist);

                                    //remove handler, which lead to stop the loop of thread
                                    loopHandler.removeCallbacksAndMessages(null);
                                    trackerHandler.removeCallbacksAndMessages(null);

                                    //stop Tracker
                                    tracker.stopTracker();
                                    Navigation.findNavController(view).navigateUp();
                                } else {
                                    Toast.makeText(getContext(),
                                            "Please add at least one bird record!",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Please select the Province!",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please select the Protocol!",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Observers must be a positive integer!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill in number of Observers!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class TrackerThread extends Thread {
        //用1000来代表经纬度错误返回值
        final double FailedResult = 1000;

        @Override
        public void run() {
            try {
                //获取时间间隔
                long sysTime = System.currentTimeMillis();
                Message msg1 = new Message();
                msg1.what = msgTime;
                msg1.obj = sysTime - startTime;
                Log.v(TAG, "sysTime：" + sysTime + " startTime：" + startTime);
                trackerHandler.sendMessage(msg1);

                //获取地理位置
                Bundle bundle = new Bundle();
                double Latitude = FailedResult;
                double Longitude = FailedResult;
                Latitude = tracker.getLatestLatitude();
                Longitude = tracker.getLatestLongitude();
                String AddressHint = tracker.getLatestAddress();
                bundle.putDouble("Latitude", Latitude);
                bundle.putDouble("Longitude", Longitude);
                bundle.putString("AddressHint", AddressHint);
                Message msg2 = new Message();
                msg2.what = msgLocation;
                msg2.obj = bundle;
                trackerHandler.sendMessage(msg2);
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }
            loopHandler.postDelayed(this, 1000);
        }
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach!");
        super.onDetach();
    }
}