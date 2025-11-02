package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "lich_day",
        foreignKeys = @ForeignKey(entity = LopHoc.class, parentColumns = "id", childColumns = "lop_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"lop_id"}), @Index(value = {"updated_at"})})
public class LichDay {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "lop_id")
    public long lopId;

    @ColumnInfo(name = "thu_trong_tuan")
    public int thuTrongTuan; // 1-7 (Thứ 2 đến CN)

    @ColumnInfo(name = "gio_bat_dau")
    public String gioBatDau; // HH:mm

    @ColumnInfo(name = "gio_ket_thuc")
    public String gioKetThuc; // HH:mm

    @ColumnInfo(name = "phong")
    public String phong;

    @ColumnInfo(name = "ghi_chu")
    public String ghiChu;

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
