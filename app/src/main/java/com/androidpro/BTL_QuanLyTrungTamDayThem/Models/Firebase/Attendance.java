package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;

import java.util.Date;

public class Attendance extends BaseEntity {
    private String lessonId;
    private String studentId;
    private boolean present;
    private Date timestamp;

    public Attendance() {

    }


    public Attendance(String lessonId, String studentId, boolean present, Date timestamp) {
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.present = present;
        this.timestamp = timestamp;
    }

    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
