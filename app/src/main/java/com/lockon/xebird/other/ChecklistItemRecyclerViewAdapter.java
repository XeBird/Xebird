package com.lockon.xebird.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.InfoShowChecklistFragment;
import com.lockon.xebird.R;
import com.lockon.xebird.db.Checklist;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChecklistItemRecyclerViewAdapter extends RecyclerView.Adapter<ChecklistItemRecyclerViewAdapter.ViewHolder> {
    public final SimpleDateFormat timeFormat;
    private final InfoShowChecklistFragment fragment;
    private final List<Checklist> mValues;

    public ChecklistItemRecyclerViewAdapter(InfoShowChecklistFragment fragment, List<Checklist> items) {
        this.fragment = fragment;
        mValues = items;
        timeFormat = new SimpleDateFormat("yyyy年MM月dd日",
                new Locale.Builder()
                        .setLanguage(PreferenceManager.getDefaultSharedPreferences(fragment.requireContext()).getString("language", "zh"))
                        .setRegion(PreferenceManager.getDefaultSharedPreferences(fragment.requireContext()).getString("country", "ch"))
                        .build());
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_checklist_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Checklist curr = mValues.get(position);
        holder.mLocation.setText(curr.getLocation());
        holder.mDate.setText(timeFormat.format(curr.getStartTime()) + "\t" + timeFormat.format(curr.getEndTime()));
        holder.mProtocal.setText(curr.getProtocol());
        holder.mView.setOnClickListener(new ItemListener(curr));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLocation;
        public final TextView mDate;
        public final TextView mProtocal;
        public Checklist mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLocation = view.findViewById(R.id.item_location);
            mDate = view.findViewById(R.id.item_date);
            mProtocal = view.findViewById(R.id.item_Protocol);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + "\t" + mDate.getText() + "\t";
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