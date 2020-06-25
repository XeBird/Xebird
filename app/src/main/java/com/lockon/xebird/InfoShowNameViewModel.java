package com.lockon.xebird;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lockon.xebird.db.BirdData;

import java.util.List;

public class InfoShowNameViewModel extends ViewModel {
    private MutableLiveData<List<BirdData>> BirdDatas;
    private MutableLiveData<String> EditText;

    public MutableLiveData<List<BirdData>> getBirdDatas() {
        if (BirdDatas == null) {
            BirdDatas = new MutableLiveData<>();
        }
        loadBirdDatas();
        return BirdDatas;
    }

    public MutableLiveData<String> getEditText() {
        if (EditText == null) {
            EditText = new MutableLiveData<>();
        }
        return EditText;
    }

    private void loadBirdDatas() {

    }
}