package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;

import java.util.List;

public class CourseViewModel extends BaseViewModel {
    private final FirebaseRepository repository;
    public MutableLiveData<List<Course>> lopHocList = new MutableLiveData<>();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new FirebaseRepository();
    }

    public void loadCoursesRealtime() {
        repository.listenCourses(new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Course> data) {
                lopHocList.postValue(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void addCourse(Course course) {
        repository.addCourses(course, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Void data) {
                notifyMessage.postValue("Thêm LopHoc thành công");
                loadCoursesRealtime();
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }
}
