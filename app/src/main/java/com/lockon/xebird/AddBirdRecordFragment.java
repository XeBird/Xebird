package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.db.BirdRecord;
import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.other.Tracker;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBirdRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBirdRecordFragment extends Fragment {
    private final String TAG = "AddBirdRecordFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BirdData = "BirdData";
    private static final String ARG_BirdRecord = "BirdRecord";

    private BirdData birdData;
    private BirdRecord birdRecord;
    public TextView nameTV, latitudeTV, longitudeTV;
    public EditText countET, locationET, commentsET;
    public Button submitBtn;
    private Tracker tracker;
    private String addressHint;

    public AddBirdRecordFragment() {
        // Required empty public constructor
    }

    public static AddBirdRecordFragment newInstance(
            BirdData birdData, BirdRecord birdRecord) {
        AddBirdRecordFragment fragment = new AddBirdRecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BirdData, birdData);
        args.putSerializable(ARG_BirdRecord, birdRecord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            birdData = (BirdData) getArguments().getSerializable(ARG_BirdData);
            birdRecord = (BirdRecord) getArguments().getSerializable(ARG_BirdRecord);
        }
        tracker = Tracker.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bird_record, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTV = view.findViewById(R.id.addBirdRecord_birdname);
        nameTV.setText(birdData.getNameCN());

        latitudeTV = view.findViewById(R.id.Latitude);
        longitudeTV = view.findViewById(R.id.Longitude);
        final double birdLatitude = tracker.getLatestLatitude();
        final double birdLongitude = tracker.getLatestLongitude();
        //用1000来代表经纬度的错误返回值
        final double FailedResult = 1000;
        if (birdLatitude != FailedResult) {
            latitudeTV.setText("LAT: " + birdLatitude);
        } else {
            latitudeTV.setText(R.string.latitude);
        }
        if (birdLongitude != FailedResult) {
            longitudeTV.setText("LAT: " + birdLongitude);
        } else {
            longitudeTV.setText(R.string.longitude);
        }

        countET = view.findViewById(R.id.addBirdRcord_birdcount);
        if (birdRecord.getBirdCount() != 0) {
            countET.setText(String.valueOf(birdRecord.getBirdCount()));
        }
        locationET = view.findViewById(R.id.addBirdRecord_location);
        String str = birdRecord.getBirdLocation();
        if (str != null && str.length() != 0) {
            locationET.setText(str);
        }

        //Address Hint
        try {
            addressHint = tracker.getLatestAddress();
            locationET.setHint(addressHint);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.auto_fill_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationET.setText(addressHint);
            }
        });

        commentsET = view.findViewById(R.id.addBirdRecord_comments);
        str = birdRecord.getBirdComments();
        if (str != null && str.length() != 0) {
            commentsET.setText(str);
        }

        submitBtn = view.findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!"".equals(countET.getText().toString())) {
                    int count = Integer.parseInt(countET.getText().toString());
                    if (count > 0) {
                        String location = locationET.getText().toString();
                        String comments = commentsET.getText().toString();

                        birdRecord.setBirdCount(count);
                        birdRecord.setBirdLatitude(birdLatitude);
                        birdRecord.setBirdLongitude(birdLongitude);
                        birdRecord.setBirdLocation(location);
                        birdRecord.setBirdComments(comments);
                        Log.i(TAG, " " + birdRecord.getUid() + " " +
                                birdRecord.getChecklistId() + " " +
                                birdRecord.getBirdId() + " " +
                                birdRecord.getBirdLatitude() + " " +
                                birdRecord.getBirdLocation());

                        BirdRecordDataBase db = BirdRecordDataBase.getInstance(getContext());
                        db.myDao().updateInBirdRecord(birdRecord);
                        Navigation.findNavController(view).navigateUp();
                        Toast.makeText(getContext(), "Add/Modify record: " + birdData.getNameCN() +
                                        ". Count: " + birdRecord.getBirdCount(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), R.string.error_count_positive_interger,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.error_count_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    final public String getTAG() {
        return TAG;
    }
}