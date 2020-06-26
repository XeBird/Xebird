package com.lockon.xebird;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.db.BirdRecord;
import com.lockon.xebird.db.BirdRecordDao;
import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.other.Tracker;

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
    private static final String ARG_ChecklistId = "ChecklistId";

    private BirdData birdData;
    private BirdRecord birdRecord;
    private String checklistId;
    public TextView nameTV, latitudeTV, longitudeTV, errorNotificationTV;
    public EditText countET, locationET, commentsET;
    public Button submitBtn;

    public AddBirdRecordFragment() {
        // Required empty public constructor
    }

    public static AddBirdRecordFragment newInstance(
            BirdData birdData, BirdRecord birdRecord, String checklistId) {
        AddBirdRecordFragment fragment = new AddBirdRecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BirdData, birdData);
        args.putSerializable(ARG_BirdRecord, birdRecord);
        args.putString(ARG_ChecklistId, checklistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            birdData = (BirdData) getArguments().getSerializable(ARG_BirdData);
            birdRecord = (BirdRecord) getArguments().getSerializable(ARG_BirdRecord);
            checklistId = getArguments().getString(ARG_ChecklistId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bird_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTV = view.findViewById(R.id.addBirdRecord_birdname);
        nameTV.setText(birdData.getNameCN());

        latitudeTV = view.findViewById(R.id.Latitude);
        longitudeTV = view.findViewById(R.id.Longitude);
        Tracker tracker = Tracker.getInstance(view.getContext().getApplicationContext());
        final double birdLatitude = tracker.getLatestLatitude();
        final double birdLongitude = tracker.getLatestLongitude();
        latitudeTV.setText(String.valueOf(birdLatitude));
        longitudeTV.setText(String.valueOf(birdLongitude));

        countET = view.findViewById(R.id.addBirdRcord_birdcount);
        locationET = view.findViewById(R.id.addBirdRecord_location);
        commentsET = view.findViewById(R.id.addBirdRecord_comments);

        errorNotificationTV = view.findViewById(R.id.error_notification);

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
                        Log.i(TAG, " " + birdRecord.getUid() + " " + birdRecord.getChecklistId() +
                                birdRecord.getBirdId() + " " + birdRecord.getBirdLatitude() + " " +
                                birdRecord.getBirdLocation());

                        BirdRecordDataBase db = BirdRecordDataBase.getInstance(getContext());
                        db.myDao().insertToBirdRecord(birdRecord);
                        Navigation.findNavController(view).navigateUp();
                    } else {
                        errorNotificationTV.setText(R.string.error_count_positive_interger);
                    }
                } else {
                    errorNotificationTV.setText(R.string.error_count_empty);
                }
            }
        });
    }

    final public String getTAG() {
        return TAG;
    }
}