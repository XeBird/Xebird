package com.lockon.xebird;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lockon.xebird.db.BirdData;

import java.io.File;

public class InfoShowDetailFragment extends Fragment {

    private final String TAG = "DetailInfo";
    private static XeBirdHandler.InfoDetailHandler handler;
    private File path2img;
    ImageView imageView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new XeBirdHandler.InfoDetailHandler(this);
        path2img = ActivityCompat.getExternalFilesDirs(requireContext(), Environment.DIRECTORY_PICTURES)[0];

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InfoShowDetailFragment.this)
                        .navigate(R.id.action_InfoShowDetailFragment_to_InfoShowNameFragment);
            }
        });

        imageView = view.findViewById(R.id.main_img);
        imageView.setImageResource(R.drawable.default_pic);//设置初始图片

        TextView textView = view.findViewById(R.id.detail_text);

        if (getArguments() != null) {
            BirdData curr = (BirdData) getArguments().getSerializable("click");
            assert curr != null;
            Log.i(TAG, "onViewCreated: set detail for " + curr.getNameCN());
            textView.setText(curr.toString());
            new Thread(new getImgAndSave("Photos", 1, curr.getNameLA(), handler, path2img)).start();
        }
    }

    public String getTAG() {
        return TAG;
    }
}
