package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.AppDatabase;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;

import java.util.List;

public class CourseRepository {
    private final CourseDAO courseDAO;
    private final LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
        allCourses = courseDAO.getAll();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
}
