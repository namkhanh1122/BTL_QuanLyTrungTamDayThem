package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;

@Entity(tableName = "tblCourse")
public class Course extends BaseEntity {
    @Ignore
    public Course() {

    }

    public Course(String name) {
        this.name = name;
    }

}
