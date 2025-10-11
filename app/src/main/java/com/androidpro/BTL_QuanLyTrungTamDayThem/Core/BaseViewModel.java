package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseViewModel extends AndroidViewModel implements NotifiableViewModel {
    protected final MutableLiveData<String> notifyMessage = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void sendNotification(String text) {
        notifyMessage.postValue(text);
    }

    @Override
    public LiveData<String> getNotifyMessage() {
        return notifyMessage;
    }
}
