package com.lockon.xebird.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.R;
import com.lockon.xebird.db.BirdRecord;

import java.util.List;

public class MyBirdRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyBirdRecordRecyclerViewAdapter.ViewHolder> {

    private final List<BirdRecord> mValues;

    public MyBirdRecordRecyclerViewAdapter(List<BirdRecord> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_record_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBirdRecordBaseInfoView.setText(String.valueOf(mValues.get(position).getUid()));
        holder.mConmment.setText(mValues.get(position).getBirdComments());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBirdRecordBaseInfoView;
        public final TextView mConmment;
        public BirdRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBirdRecordBaseInfoView = view.findViewById(R.id.item_bird_record_base_info);
            mConmment = view.findViewById(R.id.item_conment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mConmment.getText() + "'";
        }
    }
}