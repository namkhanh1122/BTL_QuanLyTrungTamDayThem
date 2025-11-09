package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import androidx.room.Entity;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;

@Entity(tableName = "users")
public class Student extends BaseEntity {
    private String courseId;


    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


}
