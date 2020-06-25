package com.lockon.xebird.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.ChecklistItemFragment;
import com.lockon.xebird.R;
import com.lockon.xebird.db.Checklist;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChecklistItemRecyclerViewAdapter extends RecyclerView.Adapter<ChecklistItemRecyclerViewAdapter.ViewHolder> {
    private final ChecklistItemFragment fragment;
    private final List<Checklist> mValues;

    public ChecklistItemRecyclerViewAdapter(ChecklistItemFragment fragment, List<Checklist> items) {
        this.fragment = fragment;
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Checklist curr = mValues.get(position);
        holder.mDateView.setText(String.valueOf(curr.getStartTime()));
        holder.mLocationView.setText(curr.getLocationName());
        holder.mProtocalView.setText(curr.getProtocol());
        holder.mView.setOnClickListener(new ItemListener(curr));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mLocationView;
        public final TextView mProtocalView;
        public Checklist mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.item_location);
            mLocationView = (TextView) view.findViewById(R.id.item_date);
            mProtocalView = (TextView) view.findViewById(R.id.item_Protocol);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + "\t" + mLocationView.getText() + "\t";
        }
    }

    class ItemListener implements View.OnClickListener {
        private String checklistUid;

        public ItemListener(Checklist checklist) {
            this.checklistUid = checklist.getUid();
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();

            bundle.putSerializable("click", checklistUid);
            NavHostFragment.findNavController(fragment).navigate(R.id.action_checklistItemFragment_to_birdRecordFragment, bundle);
        }
    }
}