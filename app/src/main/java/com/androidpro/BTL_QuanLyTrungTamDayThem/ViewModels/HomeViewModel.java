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

    private final Map<String, List<Lesson>> allLessonsMap = new HashMap<>();

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
                    allLessonsMap.clear();
                    allLessons.postValue(new ArrayList<>());
                    return;
                }

                java.util.Set<String> activeCourseIds = new java.util.HashSet<>();
                for (Course c : courses) {
                    activeCourseIds.add(c.getId());
                }

                allLessonsMap.keySet().removeIf(courseId -> !activeCourseIds.contains(courseId));

                for (String courseId : activeCourseIds) {

                    FirebaseRepository.getInstance().listenLessonsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
                        @Override
                        public void onSuccess(List<Lesson> lessonData) {
                            if (lessonData != null) {
                                allLessonsMap.put(courseId, lessonData);
                            } else {
                                allLessonsMap.remove(courseId);
                            }

                            rebuildAndPostAllLessons();
                        }

                        @Override
                        public void onError(String error) {
                            allLessonsMap.remove(courseId);
                            rebuildAndPostAllLessons();

                            notifyMessage.postValue(error);
                        }
                    });
                }

                rebuildAndPostAllLessons();
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    private void rebuildAndPostAllLessons() {
        List<Lesson> combinedList = new ArrayList<>();
        for (List<Lesson> list : allLessonsMap.values()) {
            combinedList.addAll(list);
        }

        allLessons.postValue(combinedList);
    }
}
