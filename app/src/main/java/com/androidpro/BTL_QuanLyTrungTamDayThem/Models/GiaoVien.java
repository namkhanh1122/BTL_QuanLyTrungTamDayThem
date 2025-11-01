package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "giao_vien",
        indices = {@Index(value = {"updated_at"})})
public class GiaoVien {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "ho_ten")
    public String hoTen;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "so_dien_thoai")
    public String soDienThoai;

    // Đồng bộ
    @ColumnInfo(name = "remote_id")
    public String remoteId;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    @ColumnInfo(name = "deleted")
    public boolean deleted;

    @ColumnInfo(name = "dirty")
    public boolean dirty;
}
