package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.AppDatabase;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {
    private final CourseDAO courseDAO;
    private final LiveData<List<Course>> allCourses;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        courseDAO = db.courseDAO();
        allCourses = courseDAO.getAll();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void insertAll(List<Course> courses) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(Course course : courses) {
                    courseDAO.insert(course);
                }
            }
        });
    }

    public void insert(Course course){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.insert(course);
            }
        });
    }

    public void update(Course course){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.update(course);
            }
        });
    }

    public void delete(Course course){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.delete(course);
            }
        });
    }

    public void deleteAll(){
        executorService.execute(courseDAO::deleteAll);
    }
}
