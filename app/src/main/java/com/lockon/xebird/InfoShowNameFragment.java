package com.lockon.xebird;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.other.ButtonListener;
import com.lockon.xebird.other.History;
import com.lockon.xebird.other.ItemAdapter;
import com.lockon.xebird.other.XeBirdHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InfoShowNameFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final String TAG = "NameInfo";
    public static String date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());

    private static XeBirdHandler.InfoNameHandler handler;

    public RecyclerView recyclerView;
    public ItemAdapter mAdapter;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_name, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: after navigation");
        handler = new XeBirdHandler.InfoNameHandler(this);

        EditText edittext = view.findViewById(R.id.textview_edit);
        edittext.setText(History.initInstance(getContext()).getLatestInput());

        recyclerView = view.findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemAdapter(this, new ArrayList<BirdData>());
        recyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.button_search).setOnClickListener(new ButtonListener(view, handler, getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: fragment start");
    }

    @Override
    public void onDetach() {
        BirdBaseDataBase.getInstance(this.getContext()).close();
        handler.removeMessages(XeBirdHandler.SETLIST);
        handler.removeMessages(XeBirdHandler.SETNULLTEXT);
        super.onDetach();
    }

    final public String getTAG() {
        return TAG;
    }

}
