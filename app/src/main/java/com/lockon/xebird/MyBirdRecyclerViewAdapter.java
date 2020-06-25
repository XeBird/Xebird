package com.lockon.xebird;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.db.BirdData;
import com.lockon.xebird.db.BirdRecord;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BirdRecord}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBirdRecyclerViewAdapter extends RecyclerView.Adapter<MyBirdRecyclerViewAdapter.ViewHolder> {

    private final BirdlistFragment fragment;
    public List<BirdData> mList;


    public MyBirdRecyclerViewAdapter(BirdlistFragment fragment, List<BirdData> items) {
        this.fragment = fragment;
        this.mList = items;
    }

    public void changeList(List<BirdData> l) {
        mList = l;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bird_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.birdData = mList.get(position);
        holder.name.setText(mList.get(position).getNameCN());
        holder.family.setText(mList.get(position).getFamliyCN());
        holder.details.setOnClickListener(new MyBirdRecyclerViewAdapter.DetailsListener(mList.get(position)));
        holder.add_birdRecord.setOnClickListener(new MyBirdRecyclerViewAdapter.AddBirdRecordListener(mList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView family;
        public BirdData birdData;
        public Button details, add_birdRecord;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.item_number);
            family = view.findViewById(R.id.content);
            details = view.findViewById(R.id.bird_list_details);
            add_birdRecord = view.findViewById(R.id.bird_list_add_birdRecord);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + family.getText() + "'";
        }
    }

    class DetailsListener implements View.OnClickListener {
        private BirdData birdData;

        public DetailsListener(BirdData birdData) {
            this.birdData = birdData;
        }
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();

            bundle.putSerializable("click", birdData);
            Log.i(fragment.getTAG(), "onClick: click on details of " + birdData.getNameCN());
            NavHostFragment.findNavController(fragment).navigate(R.id.action_birdlistFragment_to_InfoShowDetailFragment, bundle);
        }
    }

    class AddBirdRecordListener implements View.OnClickListener {
        private BirdRecord birdRecord;
        private BirdData birdData;

        public AddBirdRecordListener (BirdData birdData) {
            this.birdData = birdData;

        }

        @Override
        public void onClick(View v) {
            //TODO: go to the fragment to fill in the information about birdRecord.

            birdRecord = new BirdRecord(System.currentTimeMillis(), fragment.checklistId);
            birdRecord.setBirdId(birdData.getUid());
            //TODO: Save to database
        }
    }
}