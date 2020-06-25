package com.lockon.xebird;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.itemView.setOnClickListener(new MyBirdRecyclerViewAdapter.ItemListener(mList.get(position)));
        holder.name.setText(mList.get(position).getNameCN());
        holder.family.setText(mList.get(position).getFamliyCN());
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.item_number);
            family = view.findViewById(R.id.content);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + family.getText() + "'";
        }
    }

    class ItemListener implements View.OnClickListener {
        private BirdRecord birdRecord;

        public ItemListener(BirdData birdData) {
            birdRecord = new BirdRecord(System.currentTimeMillis(), fragment.checklistId);
            birdRecord.setBirdId(birdData.getUid());
        }

        @Override
        public void onClick(View v) {
            //TODO: go to the fragment to fill in the information about birdRecord.
            //TODO: Save to database
        }
    }
}