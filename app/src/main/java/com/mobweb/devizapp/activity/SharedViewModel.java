package com.mobweb.devizapp.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> base = new MutableLiveData<>();

    public void setBase(String base) {
        this.base.setValue(base);
    }

    public LiveData<String> getBase() {
        return base;
    }
}
