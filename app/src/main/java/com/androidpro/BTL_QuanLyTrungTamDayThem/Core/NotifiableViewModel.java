package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import androidx.lifecycle.LiveData;

public interface NotifiableViewModel {
    void sendNotification(String text);
    LiveData<String> getNotifyMessage();
}
