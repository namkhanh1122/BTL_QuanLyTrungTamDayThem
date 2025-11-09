package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Converters.DateConverter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "courses")
@TypeConverters(DateConverter.class)
public class Course extends BaseEntity {

    private String description;

    private Date createAt;

    private Date beginTime;

    private Date endTime;

    private ScheduleStatus status;

    private String instructorId;

    public Course() {

    }

    public Course(String name, String description, Date createAt, Date beginTime, Date endTime, ScheduleStatus status, String instructorId) {
        this.name = name;
        this.description = description;
        this.createAt = createAt;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.status = status;
        this.instructorId = instructorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

}
