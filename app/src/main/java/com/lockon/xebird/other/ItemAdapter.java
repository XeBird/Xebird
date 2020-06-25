package com.lockon.xebird.other;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lockon.xebird.InfoShowNameFragment;
import com.lockon.xebird.R;
import com.lockon.xebird.db.BirdData;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final InfoShowNameFragment infoShowNameFragment;
    public List<BirdData> mList;

    public ItemAdapter(InfoShowNameFragment infoShowNameFragment, List<BirdData> mList) {
        this.infoShowNameFragment = infoShowNameFragment;
        this.mList = mList;
    }

    public void changeList(List<BirdData> l) {
        mList = l;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_show_name_fragment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BirdData b = mList.get(position);
        holder.itemView.setOnClickListener(new ItemListener(b));
        holder.nameCN.setText(b.getNameCN());
        holder.genus.setText(b.getOrderAndFamily());
        holder.nameLA.setText(b.getNameLA());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameCN;
        public TextView genus;
        public TextView nameLA;

        public ViewHolder(View item) {
            super(item);
            this.nameCN = item.findViewById(R.id.textview_info_show_name_simple_name_cn);
            this.genus = item.findViewById(R.id.textview_info_show_name_order_and_family);
            this.nameLA = item.findViewById(R.id.textview_info_show_name_simple_name_la);

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
            Log.i(infoShowNameFragment.getTAG(), "onClick: click on " + birdData.getNameCN());
            NavHostFragment.findNavController(infoShowNameFragment).navigate(R.id.action_InfoShowNameFragment_to_InfoShowDetailFragment, bundle);
        }
    }
}
