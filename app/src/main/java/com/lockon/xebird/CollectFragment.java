package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.other.XeBirdHandler;
import com.lockon.xebird.other.getImgAndSave;

import java.io.File;

public class CollectFragment extends Fragment {
    private final String TAG = "CollectFragment";
    public ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_detail_one_view, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        XeBirdHandler.InfoDetailHandler handler = new XeBirdHandler.InfoDetailHandler(this);

        Bundle args = getArguments();
        assert args != null;
        BirdData birdData = (BirdData) args.getSerializable("BirdData");
        int position = args.getInt("Position");

        imageView = view.findViewById(R.id.main_img);
        imageView.setImageResource(R.drawable.ic_stat_name);

        TextView textView = view.findViewById(R.id.detail_text);
        assert birdData != null;

        File path2img = ActivityCompat.getExternalFilesDirs(requireContext(), Environment.DIRECTORY_PICTURES)[0];
        switch (position) {
            case 0:
                textView.setText(birdData.getSimpleName() + "\n" + birdData.getOrderAndFamily());
                new Thread(new getImgAndSave("Photos", 1, birdData.getNameLA(), handler, path2img)).start();
                break;
            case 1:
                textView.setText(birdData.getMainInfo() + "\n" + birdData.getTewwtInfo() + "\n" + birdData.getHabitInfo());
                new Thread(new getImgAndSave("Descriptive_graph", 1, birdData.getNameLA(), handler, path2img)).start();
                break;
            case 2:
                textView.setText(birdData.getRangeInfo() + "\n" + birdData.getStateInfo());
                new Thread(new getImgAndSave("Map", 1, birdData.getNameLA(), handler, path2img)).start();
                break;
            default:
                Log.i(TAG, "onViewCreated: position invalid");
        }

    }

    public String getTAG() {
        return TAG;
    }

}
