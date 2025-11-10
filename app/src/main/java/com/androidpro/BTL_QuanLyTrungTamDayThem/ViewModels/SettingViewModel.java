package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingViewModel extends BaseViewModel {
    FirebaseAuth mAuth;
    public MutableLiveData<Boolean> isLoggedOut = new MutableLiveData<>();
    public SettingViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void logoutFirebase() {
        try {
            mAuth.signOut();
            sendNotification("Đăng xuất thành công");
            isLoggedOut.postValue(true);
        } catch (Exception e) {
            sendNotification("Đăng xuất không thành công");
            isLoggedOut.postValue(false);
            Log.e("FIREBASE", "Login Fail", e);
        }
    }
}
