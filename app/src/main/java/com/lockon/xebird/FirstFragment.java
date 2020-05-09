package com.lockon.xebird;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    private dbHelper dbH;
    private TextView displaytext;
    private EditText edittext;
    private final String tag="FirstFragment";

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
        dbH=new dbHelper(getActivity());
        edittext=view.findViewById(R.id.textview_edit);
        displaytext=view.findViewById(R.id.textview_first);
        displaytext.setText(getData("石鸡"));
        view.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name= String.valueOf(edittext.getText());
                Log.d(tag, "onClick: "+Name+" was gotten!");
                displaytext.setText(getData(Name));
            }
        });
    }

    private String getData(String Name) {
        Cursor cursor=dbH.findByName(Name,"MainDATA");
        String visiText="";
        if(cursor!=null&&cursor.moveToFirst()){
            visiText=cursor.getString(cursor.getColumnIndex("NAME_CN"))
            +"\t"+cursor.getString(cursor.getColumnIndex("NAME_EN"))
            +"\n"+cursor.getString(cursor.getColumnIndex("NAME_LA"))
            +"\n"+cursor.getString(cursor.getColumnIndex("MAIN_INFO"));
        }
        return visiText;
    }

}
