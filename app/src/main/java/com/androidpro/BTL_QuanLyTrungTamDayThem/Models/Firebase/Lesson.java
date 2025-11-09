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
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getCourseId() {return courseId;}

    public void setCourseId(String courseId) {this.courseId = courseId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return Objects.equals(getId(), lesson.getId()) &&
                Objects.equals(title, lesson.title) &&
                Objects.equals(content, lesson.content) &&
                Objects.equals(videoUrl, lesson.videoUrl) &&
                Objects.equals(beginTime, lesson.beginTime) &&
                Objects.equals(endTime, lesson.endTime) &&
                status == lesson.status;
    }
}
