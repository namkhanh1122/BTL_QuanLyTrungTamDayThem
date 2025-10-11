package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM tblCourse")
    void deleteAll();

    @Query("SELECT * FROM tblCourse ORDER BY name ASC")
    LiveData<List<Course>> getAll();
}
