package com.lockon.xebird.other;

import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.lockon.xebird.InfoShowDetailFragment;
import com.lockon.xebird.R;

public class returnLinstener implements View.OnClickListener {
    private InfoShowDetailFragment mFragment;

    public returnLinstener(InfoShowDetailFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void onClick(View v) {
        NavHostFragment.findNavController(mFragment).navigate(R.id.action_InfoShowDetailFragment_to_InfoShowNameFragment);

    }
}
