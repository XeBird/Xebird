package com.lockon.xebird;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdData;

import java.util.ArrayList;
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
    private RecyclerView recyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


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

        recyclerView = view.findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemAdapter(new ArrayList<BirdData>());
        recyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.button_search).setOnClickListener(new ButtonListener(view, handler, getContext()));
    }

    @Override
    public void onDetach() {
        BirdBaseDataBase.getInstance(this.getContext()).close();
        super.onDetach();
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        public List<BirdData> mList;

        public ItemAdapter(List<BirdData> mList) {
            this.mList = mList;
        }

        public void changeList(List<BirdData> l) {
            mList = l;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ex, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BirdData b = mList.get(position);
            holder.itemView.setOnClickListener(new ItemListener(b));
            holder.name.setText(b.getSimpleName());
            holder.genus.setText(b.getGenusCN());
            holder.order.setText(b.getOrderAndFamily());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView genus;
            public TextView order;

            public ViewHolder(View item) {
                super(item);
                this.name = item.findViewById(R.id.textView_name);
                this.genus = item.findViewById(R.id.genus);
                this.order = item.findViewById(R.id.family_order);
            }
        }

        class ItemListener implements View.OnClickListener {
            private BirdData birdData;

            public ItemListener(BirdData birdData) {
                this.birdData = birdData;
            }

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putSerializable("click", birdData);
                Log.i(TAG, "onClick: click on " + birdData.getNameCN());
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        }
    }

}
