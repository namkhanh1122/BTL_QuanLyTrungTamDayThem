package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "bai_kiem_tra",
        foreignKeys = @ForeignKey(entity = LopHoc.class, parentColumns = "id", childColumns = "lop_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"lop_id"}), @Index(value = {"updated_at"})})
public class BaiKiemTra {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "lop_id")
    public long lopId;

    @ColumnInfo(name = "ten_bai")
    public String tenBai;

    @ColumnInfo(name = "he_so")
    public float heSo;

    @ColumnInfo(name = "diem_toi_da")
    public float diemToiDa;

    @ColumnInfo(name = "han_nop")
    public String hanNop; // yyyy-MM-dd

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
