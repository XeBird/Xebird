package com.lockon.xebird.other;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lockon.xebird.BirdlistFragment;
import com.lockon.xebird.CollectFragment;
import com.lockon.xebird.db.BirdData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kotlin.TypeCastException;

import static com.lockon.xebird.R.drawable.no_bitmap;

public class XeBirdHandler {
    public static final int SETBITMAP = 0;
    public static final int SETNULLTEXT = 1;
    public static final int SETLIST = 2;
    public static final int SETNULLBITMAP = 3;
    public static final int INFONAME = 4;
    public static final int INFODETAIL = 5;

    static abstract class BaseHandler extends Handler {
        public WeakReference<Fragment> mFragment;
        public String TAG;
    }


    public static class InfoDetailHandler extends BaseHandler {
        public InfoDetailHandler(CollectFragment f) {
            this.mFragment = new WeakReference<Fragment>(f);
            this.TAG = f.getTAG();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            CollectFragment f = (CollectFragment) mFragment.get();
            assert f != null;
            switch (msg.what) {
                case SETBITMAP:
                    Log.i(TAG, "handleMessage: set bitmap");
                    f.imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                case SETNULLBITMAP:
                    Log.i(TAG, "handleMessage: set NULL bitmap");
                    f.imageView.setImageResource(no_bitmap);
            }
        }
    }

    public static class BirdlistHandler extends BaseHandler {
        public BirdlistHandler(BirdlistFragment fragment) {
            this.mFragment = new WeakReference<Fragment>(fragment);
            this.TAG = fragment.getTAG();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            BirdlistFragment f = (BirdlistFragment) mFragment.get();
            assert f != null;
            switch (msg.what) {
                case SETNULLTEXT:
                    Log.i(TAG, "handleMessage: null get");
                    List<BirdData> bs_null = new ArrayList<>();
                    f.mAdapter.changeList(bs_null);
                    break;
                case SETLIST:
                    if (msg.obj instanceof List<?>) {
                        List<BirdData> bs = (List<BirdData>) msg.obj;
                        for (BirdData b : bs) {
                            Log.i(TAG, "handleMessage: data a ru " + b.getNameCN());
                        }
                        f.mAdapter.changeList(bs);
                    } else {
                        throw new TypeCastException();
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    }
}
