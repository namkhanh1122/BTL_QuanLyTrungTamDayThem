package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "diem_danh",
        foreignKeys = {
                @ForeignKey(entity = LopHoc.class, parentColumns = "id", childColumns = "lop_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = HocVien.class, parentColumns = "id", childColumns = "hoc_vien_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = {"lop_id"}), @Index(value = {"hoc_vien_id"}), @Index(value = {"ngay"}), @Index(value = {"updated_at"})})
public class DiemDanh {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "lop_id")
    public long lopId;

    @ColumnInfo(name = "hoc_vien_id")
    public long hocVienId;

    @ColumnInfo(name = "ngay")
    public String ngay; // yyyy-MM-dd

    @ColumnInfo(name = "trang_thai")
    public String trangThai; // co_mat/vang/phep

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
