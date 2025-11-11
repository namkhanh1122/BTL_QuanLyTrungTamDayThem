package com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase;

import androidx.room.Entity;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseEntity;

@Entity(tableName = "documents")
public class Document extends BaseEntity {
    private String title;

    private String description;

    private String url;

    private String lessonId;

    public Document() {

    }

    public Document(String title, String description, String url, String lessonId) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

}
