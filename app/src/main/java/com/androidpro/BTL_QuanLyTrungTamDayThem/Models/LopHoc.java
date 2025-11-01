package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "lop_hoc",
        indices = {@Index(value = {"giao_vien_id"}), @Index(value = {"updated_at"})})
public class LopHoc {
    @PrimaryKey(autoGenerate = true) public long id;

    @ColumnInfo(name = "ten_lop")  public String tenLop;
    @ColumnInfo(name = "mon_hoc")  public String monHoc;
    @ColumnInfo(name = "cap_lop")  public String capLop;
    @ColumnInfo(name = "mo_ta")    public String moTa;

    @ColumnInfo(name = "giao_vien_id") public long giaoVienId;

    // đồng bộ
    @ColumnInfo(name = "remote_id")  public String remoteId;
    @ColumnInfo(name = "updated_at") public long updatedAt;  // epoch millis
    @ColumnInfo(name = "deleted")    public boolean deleted;
    @ColumnInfo(name = "dirty")      public boolean dirty;
}
