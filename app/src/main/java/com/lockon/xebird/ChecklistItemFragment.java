package com.lockon.xebird;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdRecordDataBase;
import com.lockon.xebird.db.Checklist;
import com.lockon.xebird.other.ChecklistItemRecyclerViewAdapter;

import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class ChecklistItemFragment extends Fragment {

    private static final String NUM = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChecklistItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChecklistItemFragment newInstance(int columnCount) {
        ChecklistItemFragment fragment = new ChecklistItemFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            BirdRecordDataBase bd = BirdRecordDataBase.getInstance(requireContext());
            List<Checklist> whatget = bd.myDao().getAllChecklist();


            recyclerView.setAdapter(new ChecklistItemRecyclerViewAdapter(this, whatget));
        }
        return view;
    }
}