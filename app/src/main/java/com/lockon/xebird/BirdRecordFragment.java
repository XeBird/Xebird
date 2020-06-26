package com.lockon.xebird;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdRecord;
import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.other.MyBirdRecordRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BirdRecordFragment extends Fragment {
    private final static String TAG = "BirdRecordFragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BirdRecordFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BirdRecordFragment newInstance(int columnCount) {
        BirdRecordFragment fragment = new BirdRecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);
        List<BirdRecord> whatget = new ArrayList<>();
        if (getArguments() != null) {
            String checklistId = (String) getArguments().getSerializable("click");
            Log.i(TAG, "onCreateView: get chectlist id " + checklistId);
            BirdRecordDataBase bd = BirdRecordDataBase.getInstance(requireContext());
            whatget = bd.myDao().getAllByCid(checklistId);

        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyBirdRecordRecyclerViewAdapter(whatget, requireContext()));
        }
        return view;
    }
}