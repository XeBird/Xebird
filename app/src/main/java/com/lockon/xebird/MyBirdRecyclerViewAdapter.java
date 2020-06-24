package com.lockon.xebird;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lockon.xebird.db.BirdRecord;
import com.lockon.xebird.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BirdRecord}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBirdRecyclerViewAdapter extends RecyclerView.Adapter<MyBirdRecyclerViewAdapter.ViewHolder> {

    private final List<BirdRecord> mValues;
    private final BirdlistFragment fragment;


    public MyBirdRecyclerViewAdapter(BirdlistFragment fragment, List<BirdRecord> items) {
        this.fragment = fragment;
        this.mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_birdlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getBirdName());
        holder.mContentView.setText(mValues.get(position).getBirdCount());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public BirdRecord mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}