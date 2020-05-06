package com.lockon.xebird;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

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
        TextView text= view.findViewById(R.id.textview_first);
        String path=CopyDB.DB_PATH+"/"+CopyDB.DB_NAME;
        SQLiteDatabase a=SQLiteDatabase.openOrCreateDatabase(path,null);
        String sqlstatement="select * from MainDATA limit 5";
        Cursor cursor=a.rawQuery(sqlstatement,null);
        if(cursor!=null&&cursor.moveToFirst()){
            text.setText(cursor.getString(cursor.getColumnIndex("NAME_CN")));
        }
    }
}
