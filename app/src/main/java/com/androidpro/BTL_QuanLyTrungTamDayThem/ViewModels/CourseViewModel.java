package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course.CourseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;

import java.util.List;

public class CourseViewModel extends BaseViewModel {
    public MutableLiveData<List<Course>> courseList = new MutableLiveData<>();
    public MutableLiveData<Course> courseDetails = new MutableLiveData<>();
    public MutableLiveData<List<Lesson>> lessonList = new MutableLiveData<>();
    public MutableLiveData<List<Student>> studentList = new MutableLiveData<>();
    private CourseRepository repository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
    }

    public void loadCourseDetails(String courseId) {
        FirebaseRepository.getInstance().getCourseById(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Course data) {
                courseDetails.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void loadLessonsForCourse(String courseId) {
        FirebaseRepository.getInstance().listenLessonsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Lesson> data) {
                lessonList.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void loadStudentForCourse(String courseId) {
        FirebaseRepository.getInstance().listenStudentsInCourses(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Student> data) {
                studentList.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void loadCoursesRealtime() {
        FirebaseRepository.getInstance().listenCourses(new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Course> data) {
                courseList.postValue(data);
                //repository.insertAll(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void addCourse(Course course) {
        FirebaseRepository.getInstance().addCourses(course, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Course data) {
                notifyMessage.postValue("Thêm lớp học thành công");
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }

    public void deleteCourse(String courseId) {
        FirebaseRepository.getInstance().deleteCourse(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Course data) {
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }

    public void updateCourse(Course course) {
        FirebaseRepository.getInstance().updateCourse(course, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Course data) {
                notifyMessage.postValue("Cập nhật khóa học thành công");
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }
}
