package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Converters.DateConverter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;

import java.util.Date;

@Entity(tableName = "attendances")
@TypeConverters(DateConverter.class)
public class Attendance extends BaseEntity {
    private String lessonId;
    private String studentId;
    private boolean present;
    private Date timestamp;
    public double score;

    public Attendance() {

    }

    public Attendance(String lessonId, String studentId, boolean present, Date timestamp, double score) {
        this.lessonId = lessonId;
        this.studentId = studentId;
        this.present = present;
        this.timestamp = timestamp;
        this.score = score;
    }

    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public double getScore() {return score;}
    public void setScore(double score) {this.score = score;}
}
