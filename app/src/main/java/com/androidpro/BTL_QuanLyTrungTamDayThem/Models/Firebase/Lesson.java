package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Converters.DateConverter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "lessons")
@TypeConverters(DateConverter.class)
public class Lesson extends BaseEntity {
    private String title;

    private String content;

    private String videoUrl;

    private Date beginTime;

    private Date endTime;

    private ScheduleStatus status;

    private String courseId;

    public Lesson() {

    }

    public Lesson( String title, String content, String videoUrl, Date beginTime, Date endTime, ScheduleStatus status) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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
        if (this.status == ScheduleStatus.Canceled) {
            return ScheduleStatus.Canceled;
        }

        Date now = new Date();

        if (getBeginTime() == null || getEndTime() == null) {
            return ScheduleStatus.Planned;
        }

        if (now.after(getEndTime())) {
            return ScheduleStatus.Completed;
        }

        if (now.after(getBeginTime())) {
            return ScheduleStatus.Active;
        }

        return ScheduleStatus.Planned;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getCourseId() {return courseId;}

    public void setCourseId(String courseId) {this.courseId = courseId;}

}
