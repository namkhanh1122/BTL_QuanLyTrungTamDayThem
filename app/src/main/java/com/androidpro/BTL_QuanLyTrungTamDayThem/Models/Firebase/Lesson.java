package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;

import java.util.Date;

public class Lesson extends BaseEntity {
    private String title;

    public String content;

    public String videoUrl;

    public Date beginTime;

    public Date endTime;

    public String courseId;

    public ScheduleStatus status;

    public Lesson() {

    }

    public Lesson(String title, String content, String videoUrl, Date beginTime, Date endTime, String courseId, ScheduleStatus status) {
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.courseId = courseId;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }
}
