package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course.CourseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;
import java.util.List;

public class CourseListViewModel extends BaseViewModel {
    private final CourseRepository repo;
    private final LiveData<List<Course>> courses;

    public CourseListViewModel(@NonNull Application app) {
        super(app);
        repo = new CourseRepository(app);
        courses = repo.getAllCourses();
    }

    public LiveData<List<Course>> getCourses() { return courses; }
}
