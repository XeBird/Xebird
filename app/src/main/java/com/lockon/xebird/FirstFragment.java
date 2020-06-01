package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;

import java.util.List;

public class FirstFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final String TAG="FirstFragment";

    static final int SETBITMAP=0;
    static final int SETNULLTEXT = 1;
    static final int SETLIST = 2;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETNULLTEXT:
                    mAdapter.changeList(null);
                    break;
                case SETLIST:
                    List<BirdData> bs = (List<BirdData>) msg.obj;
                    for (BirdData b : bs) {
                        Log.i(TAG, "handleMessage: data a ru " + b.getNameCN());
                    }
                    mAdapter.changeList(bs);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };
    private ItemAdapter mAdapter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText edittext = view.findViewById(R.id.textview_edit);
        edittext.setText(History.initInstance(getContext()).getLatestInput());

        View v = getLayoutInflater().inflate(R.layout.fragment_first, null);
        ListView listView = v.findViewById(R.id.list_view);
        BirdBaseDataBase bd = BirdBaseDataBase.getInstance(getContext());
        mAdapter = new ItemAdapter(getContext(), bd.myDao().findByNameCN("鹌鹑"));
        listView.setAdapter(mAdapter);

        view.findViewById(R.id.button_search).setOnClickListener(new ButtonListener(getActivity(), view, handler, getContext()));
    }

    @Override
    public void onDetach() {
        BirdBaseDataBase.getInstance(this.getContext()).close();
        super.onDetach();
    }

    class ItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        private List<BirdData> list;
        private LayoutInflater mInflater;

        ItemAdapter(Context context, List<BirdData> list) {
            super();
            this.list = list;
            this.mInflater = LayoutInflater.from(context);
        }

        void changeList(List<BirdData> list) {
            list.clear();
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public BirdData getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_ex, null);
                holder.name = (TextView) convertView.findViewById(R.id.textView_name);
                holder.genus = (TextView) convertView.findViewById(R.id.genus);
                holder.order = (TextView) convertView.findViewById(R.id.family_order);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            BirdData curr = list.get(position);
            holder.name.setText(curr.getSimpleName());
            holder.genus.setText(curr.getGenusCN());
            holder.order.setText(curr.getOrderAndFamily());

            return convertView;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BirdData clicked = getItem(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("click", clicked);
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        }

        final class ViewHolder {
            private TextView name;
            private TextView genus;
            private TextView order;
        }
    }

}
