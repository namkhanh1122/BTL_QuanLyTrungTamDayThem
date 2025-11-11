package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends BaseViewModel {
    public MutableLiveData<List<Lesson>> allLessons = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadLessonsRealtime() {
        String instructorId = FirebaseAuth.getInstance().getUid();
        if (instructorId == null) {
            notifyMessage.postValue("Không xác định được giảng viên");
            return;
        }

        FirebaseRepository.getInstance().listenCoursesForInstructor(instructorId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                if (courses == null || courses.isEmpty()) {
                    allLessons.postValue(new ArrayList<>());
                    return;
                }

                for (Course c : courses) {
                    String courseId = c.getId();
                    FirebaseRepository.getInstance().listenLessonsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
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

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }
}
