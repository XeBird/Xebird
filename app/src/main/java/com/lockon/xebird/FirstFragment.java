package com.lockon.xebird;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.lockon.xebird.db.MyDataBase;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class FirstFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private EditText edittext;
    private final String TAG="FirstFragment";
    private ImageView imgView ;

    static final int SETBITMAP=0;
    static final int SETNULLTEXT=1;
    @SuppressLint("HandlerLeak")
    private Handler handler= new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETBITMAP:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imgView.setImageBitmap(bitmap);
                    break;
                case SETNULLTEXT:
                    displaytext.setText(R.string.null_input);
            }
        }
    };
    private TextView displaytext;
    private MyDataBase db;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        if(getContext()==null){
            Log.i(TAG, "onViewCreated: ");
        }else {
            db= Room.databaseBuilder(getContext(),
                    MyDataBase.class, "Bird").build();
        }

        imgView=view.findViewById(R.id.main_img);
        displaytext = view.findViewById(R.id.textview_first);
        Log.i(TAG, "onViewCreated: imgview create success");

        imgView.setImageResource(R.drawable.default_pic);//设置初始图片
        view.findViewById(R.id.button_search).setOnClickListener(new ButtonListener(view,db,handler));

        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        },1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                //TODO：显示申请成功与否，后续会删除
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this.getActivity(), "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Log.i(TAG, "onRequestPermissionsResult: rejectede");
        }
    }
}
