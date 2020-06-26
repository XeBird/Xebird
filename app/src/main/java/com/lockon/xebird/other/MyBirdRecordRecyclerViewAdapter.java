package com.lockon.xebird.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.R;
import com.lockon.xebird.db.BirdBaseDataBase;
import com.lockon.xebird.db.BirdRecord;

import java.util.List;

public class MyBirdRecordRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0;
    public static final int LIST_ITEM = 1;
    private final List<BirdRecord> mValues;
    private final Context context;

    public MyBirdRecordRecyclerViewAdapter(List<BirdRecord> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return LIST_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == LIST_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_show_record_fragment, parent, false);
            return new ListViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_show_birdrecord_header, parent, false);
            return new HeaderViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).mBirdRecordBaseInfoView.setText(BirdBaseDataBase.getInstance(context)
                    .myDao().findByUid(mValues.get(position - 1).getBirdId()).getNameCN());
            ((ListViewHolder) holder).mCount.setText(String.valueOf(mValues.get(position - 1).getBirdCount()));
        }
    }


    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mName;
        public TextView mCount;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.item_name_label);
            mCount = itemView.findViewById(R.id.item_count_label);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBirdRecordBaseInfoView;
        public final TextView mCount;

        public ListViewHolder(View view) {
            super(view);
            mView = view;
            mBirdRecordBaseInfoView = view.findViewById(R.id.item_name);
            mCount = view.findViewById(R.id.item_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCount.getText() + "'";
        }
    }
}