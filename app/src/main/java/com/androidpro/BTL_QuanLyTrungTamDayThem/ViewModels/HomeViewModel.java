package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;

import java.util.List;

public class HomeViewModel extends BaseViewModel {
    public MutableLiveData<List<Lesson>> allLessons = new MutableLiveData<>();
    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadLessonsRealtime() {
        FirebaseRepository.getInstance().listenLessonsRealtime(new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Lesson> data) {
                allLessons.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }
}
