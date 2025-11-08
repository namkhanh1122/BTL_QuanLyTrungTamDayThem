package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LopHoc;

import java.util.List;

public class CourseViewModel extends BaseViewModel {
    private final FirebaseRepository repository;
    public MutableLiveData<List<LopHoc>> lopHocList = new MutableLiveData<>();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new FirebaseRepository();
    }


    public void loadLopHocs() {
        repository.getAllLopHoc(new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<LopHoc> data) {
                lopHocList.postValue(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void addLopHoc(LopHoc LopHoc) {
        repository.addLopHoc(LopHoc, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Void data) {
                notifyMessage.postValue("Thêm LopHoc thành công");
                loadLopHocs(); // refresh list
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }
}
