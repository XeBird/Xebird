package com.lockon.xebird;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.lockon.xebird.FirstFragment.SETBITMAP;
import static com.lockon.xebird.FirstFragment.SETNULLTEXT;
import static com.lockon.xebird.FirstFragment.SETTEXT;


public class ButtonListener implements View.OnClickListener {
    private final String TAG = "ButtonLis";
    private Handler han;
    private View view;
    private final String path2Img =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"Xebird";

    ButtonListener(View v, Handler handler) {
        this.han = handler;
        this.view = v;
    }

    @Override
    public void onClick(View v) {
        File path = new File(path2Img);
        Log.i(TAG, "ButtonListener: file will be save in "+path2Img);
        EditText edit=view.findViewById(R.id.textview_edit);
        Editable input=edit.getText();
        if(input==null){
            han.sendEmptyMessage(SETNULLTEXT);
            Log.i(TAG, "onClick: input nothing");
        }else{
            Log.i(TAG, "onClick: get edittext " + input);
            BirdBaseDataBase db=BirdBaseDataBase.getInstance(view.getContext());
            BirdData whatGet = db.myDao().findByNameCN(input.toString());
            if(whatGet==null){
                han.sendEmptyMessage(SETNULLTEXT);
                Log.i(TAG, "onClic: nothing get");
            }else {
                Message msgWithString = Message.obtain(han);
                msgWithString.what = SETTEXT;
                msgWithString.obj = whatGet.toString();
                msgWithString.sendToTarget();
                Log.i(TAG, "onClick: send detail info to FirstFragment");
                new Thread(new saveImgToLocal("Map", 1, whatGet, han,path2Img)).start();
            }
        }

    }

    static class saveImgToLocal implements Runnable {

        private final String Host = "https://xebird.proto.cf/";
        private final String Path;
        private final String DMP;
        private final int index;
        private final BirdData input;
        private final String TAG = "WebRequest";
        private final Handler handler;
        private final String path2Img;

        saveImgToLocal(String path, int index, BirdData input, Handler han, String path2Img) {
            String DMP1;
            Path = path;
            switch (Path) {
                case "Descriptive_graph":
                    DMP1 = "-D-";
                    break;
                case "Map":
                    DMP1 = "-M-";
                    break;
                case "Photo":
                    DMP1 = "-P-";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + Path);
            }
            DMP = DMP1;
            this.index = index;
            this.handler = han;
            this.input=input;
            this.path2Img=path2Img;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            
            String NameLA = input.getNameLA().replace(" ","_");
            Log.i(TAG, "onClick: get name in LA called " + NameLA);
            File localImg = new File(path2Img + File.separator + NameLA + DMP + index+ ".jpg");
            if (!localImg.exists()) {
                Log.i(TAG, "onClick: local file dont exist");
                String totalWeb = Host + Path + "/" + NameLA + DMP + index + ".jpg";
                Log.i(TAG, "getImgFromWeb: download from " + totalWeb);
                Bitmap bitmap = null;
                try {
                    //网络请求
                    URL url = new URL(totalWeb);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setConnectTimeout(6000);//设置超时
                    conn.setDoInput(true);
                    conn.setUseCaches(false);//不缓存
                    Log.i(TAG, "getImgFromWeb: download begin");
                    conn.connect();
                    InputStream is = conn.getInputStream();//获得图片的数据流
                    bitmap = BitmapFactory.decodeStream(is);//读取图像数据
                    is.close();
                    Log.i(TAG, "getImgFromWeb: download end");
                    Log.i(TAG, "getImgFromWeb: save pic to local file " + localImg.toString());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localImg));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                    Log.i(TAG, "getImgFromWeb: save pic to local file success");
                } catch (IOException e) {
                    if (bitmap == null) {
                        Log.i(TAG, "getImgFromWeb: get pic from web failed");
                    } else {
                        Log.i(TAG, "getImgFromWeb: save pic to local file failed");
                    }
                    e.printStackTrace();
                }
                Message msg = Message.obtain(handler);
                msg.what = SETBITMAP;
                msg.obj = bitmap;
                Log.i(TAG, "getImgFromWeb: send message");
                msg.sendToTarget();
            }else {
                Message msg=Message.obtain(handler);
                msg.what=SETBITMAP;
                msg.obj=BitmapFactory.decodeFile(localImg.getPath());
                Log.i(TAG, "getImgFromWeb: send message from local");
                msg.sendToTarget();
            }
        }

        /* Checks if external storage is available for read and write */
        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        /* Checks if external storage is available to at least read */
        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }
    }
}
