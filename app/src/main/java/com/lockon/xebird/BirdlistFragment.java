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
import com.lockon.xebird.db.Checklist;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BirdlistFragment extends Fragment {
    private static final String TAG = "BirdlistFragment";


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CHECKLIST = "checklist";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Checklist checklist;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BirdlistFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BirdlistFragment newInstance(int columnCount, Bundle mBundle) {
        BirdlistFragment fragment = new BirdlistFragment();
        Bundle args = new Bundle();
//        if (mBundle != null) {
//            Log.i(TAG, "Successfully get mBundle!");
//        }
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putSerializable(ARG_CHECKLIST, (Checklist) mBundle.getSerializable("checklist"));
        fragment.setArguments(args);
        if (fragment.getArguments() != null) {
            Log.i(TAG, "newInstance: Successfully getArguments!");
        } else {
            Log.i(TAG, "newInstance: Failed to getArguments!");
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            checklist = (Checklist) getArguments().getSerializable(ARG_CHECKLIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView!");
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.i(TAG, "onCreateView: getArguments Successfully!");
        } else {
            Log.i(TAG, "onCreateView: getArguments Failed!");
        }
        //mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        checklist = (Checklist) getArguments().getSerializable(ARG_CHECKLIST);
        if (checklist != null) {
            Log.i(TAG, "onCreateView: Successfully get checklist!");
        } else {
            Log.i(TAG, "onCreateView: Failed to get checklist!");
        }

        View view = inflater.inflate(R.layout.fragment_birdlist_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            List<BirdRecord> whatget = (List<BirdRecord>) checklist.getBirdList(); //TODO：确保getBirdList() 返回非空（建立一个默认对象？）
            recyclerView.setAdapter(new MyBirdRecyclerViewAdapter(this, whatget));
            //TODO：
        }
        return view;
    }
}