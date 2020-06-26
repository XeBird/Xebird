package com.lockon.xebird;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.other.History;
import com.lockon.xebird.other.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class InfoShowNameFragment extends Fragment {
    private static final String TAG = "InfoShownameFragment";

    private InfoShowNameViewModel mViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemAdapter mAdapter;
    private BirdBaseDataBase bd;
    private EditText edittext;

    public static InfoShowNameFragment newInstance() {
        return new InfoShowNameFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(InfoShowNameViewModel.class);

        final Observer<List<BirdData>> listObserver = new Observer<List<BirdData>>() {
            @Override
            public void onChanged(List<BirdData> s) {
                mAdapter.changeList(s);
            }
        };

        mViewModel.getBirdDatas().observe(this, listObserver);

        final Observer<String> editObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) {
                    mViewModel.getBirdDatas().postValue(new ArrayList<BirdData>());
                } else {
                    List<BirdData> whatget = bd.myDao().findByNameCN(s.trim());
                    mViewModel.getBirdDatas().postValue(whatget);
                }
            }
        };

        mViewModel.getEditText().observe(this, editObserver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bd = BirdBaseDataBase.getInstance(requireContext());

        edittext = view.findViewById(R.id.textview_edit);
        edittext.setText(History.initInstance(getContext()).getLatestInput());

        recyclerView = view.findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemAdapter(mViewModel.getBirdDatas().getValue(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));

        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getEditText().postValue(String.valueOf(edittext.getText()));
            }
        });

    }

    public static String getTAG() {
        return TAG;
    }
}