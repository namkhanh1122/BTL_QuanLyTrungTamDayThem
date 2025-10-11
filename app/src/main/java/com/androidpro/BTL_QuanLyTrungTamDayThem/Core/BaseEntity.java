package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

}
