package com.lockon.xebird;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lockon.xebird.db.BirdData;

public class InfoShowDetailFragment extends Fragment {

    private final String TAG = "DetailInfo";
    CollectionAdapter collectionAdapter;
    ViewPager2 viewPager2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            BirdData curr = (BirdData) getArguments().getSerializable("click");
            assert curr != null;
            String name = curr.getNameCN();
            Log.i(TAG, "onViewCreated: set detail for " + name);

            collectionAdapter = new CollectionAdapter(this, curr);
            viewPager2 = view.findViewById(R.id.pager);
            viewPager2.setAdapter(collectionAdapter);
            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            new TabLayoutMediator(tabLayout, viewPager2,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            switch (position) {
                                case 0:
                                    tab.setText("Photo");
                                    break;
                                case 1:
                                    tab.setText("Main Info");
                                    break;
                                case 2:
                                    tab.setText("Range");
                                    break;
                                default:
                                    Log.i(TAG, "onConfigureTab: invalid position");
                            }
                        }
                    }
            ).attach();
        } else {
            Log.i(TAG, "onViewCreated: Arg is null");
        }
    }

    public String getTAG() {
        return TAG;
    }

    private static class CollectionAdapter extends FragmentStateAdapter {

        private BirdData birdData;

        public CollectionAdapter(@NonNull Fragment fragment, BirdData curr) {
            super(fragment);
            birdData = curr;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new CollectFragment();
            Bundle args = new Bundle();
            args.putSerializable("BirdData", birdData);
            args.putInt("Position", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
