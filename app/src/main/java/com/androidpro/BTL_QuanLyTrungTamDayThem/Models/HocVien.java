package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "hoc_vien",
        indices = {@Index(value = {"updated_at"})})
public class HocVien {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "ho_ten")
    public String hoTen;

    @ColumnInfo(name = "ma_hoc_vien")
    public String maHocVien;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "so_dien_thoai")
    public String soDienThoai;

    @ColumnInfo(name = "phu_huynh")
    public String phuHuynh;

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
