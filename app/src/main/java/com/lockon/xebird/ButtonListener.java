package com.lockon.xebird;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;

import java.io.File;
import java.util.List;

import static com.lockon.xebird.XeBirdHandler.SETLIST;
import static com.lockon.xebird.XeBirdHandler.SETNULLTEXT;

public class ButtonListener implements View.OnClickListener {
    private final String TAG = "ButtonLis";
    private Handler han;
    private View view;
    private Context context;
    private final File path2Img;

    ButtonListener(View v, Handler handler, Context context) {
        this.han = handler;
        this.view = v;
        this.context = context;
        this.path2Img = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)[0];
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "ButtonListener: file will be save in "+path2Img);
        EditText edit=view.findViewById(R.id.textview_edit);
        Editable input=edit.getText();
        if(input==null){
            han.sendEmptyMessage(SETNULLTEXT);
            Log.i(TAG, "onClick: input nothing");
        }else {
            Log.i(TAG, "onClick: get edittext " + input);
            BirdBaseDataBase db = BirdBaseDataBase.getInstance(view.getContext());
            List<BirdData> whatGet = db.myDao().findByNameCN(input.toString());

            if (whatGet.size() == 0) {
                han.sendEmptyMessage(SETNULLTEXT);
                Log.i(TAG, "onClic: nothing get");
            } else {
                Message msgWithString = Message.obtain(han);
                msgWithString.what = SETLIST;
                msgWithString.obj = whatGet;
                msgWithString.sendToTarget();
                Log.i(TAG, "onClick: send name info to InfoShowNameFragment");

            }
        }

        History.initInstance(context).put(input);
    }

    public boolean checkPression() {
        int hasReadPression = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWritePression = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasInterPression = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.INTERNET);
        boolean res = hasInterPression == PackageManager.PERMISSION_GRANTED && hasReadPression == PackageManager.PERMISSION_GRANTED && hasWritePression == PackageManager.PERMISSION_GRANTED;
        Log.i(TAG, "checkPression: Pression is " + res);
        return res;
    }

}

