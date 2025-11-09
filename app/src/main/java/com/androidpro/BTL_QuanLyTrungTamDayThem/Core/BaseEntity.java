package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.Date;

public abstract class BaseEntity {
    @PrimaryKey
    @NonNull
    protected String id;

    protected String name;

    protected Date lastUpdated = new Date();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

}
