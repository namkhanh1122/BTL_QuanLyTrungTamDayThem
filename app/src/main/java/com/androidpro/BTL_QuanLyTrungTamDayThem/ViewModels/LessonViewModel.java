package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;

import java.util.List;

public class LessonViewModel extends BaseViewModel {
    public MutableLiveData<Lesson> lessonDetail = new MutableLiveData<>();
    public MutableLiveData<List<Lesson>> lessonList = new MutableLiveData<>();
    public MutableLiveData<List<Student>> studentList = new MutableLiveData<>();
    public MutableLiveData<List<Attendance>> attendanceList = new MutableLiveData<>();

    public LessonViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadLessonDetails(String lessonId) {
        FirebaseRepository.getInstance().getLessonById(lessonId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson data) {
                lessonDetail.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void loadLessonsInCourseRealtime(String courseId) {
        FirebaseRepository.getInstance().listenLessonsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Lesson> data) {
                lessonList.postValue(data);
                //repository.insertAll(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }


    public void addLesson(String courseId, Lesson lesson) {
        FirebaseRepository.getInstance().addLessonToCourse( courseId, lesson, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson data) {
                notifyMessage.postValue("Thêm buổi học thành công");
                loadLessonsInCourseRealtime(courseId);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }
}
