package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "diem",
        foreignKeys = {
                @ForeignKey(entity = BaiKiemTra.class, parentColumns = "id", childColumns = "bai_kiem_tra_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = HocVien.class, parentColumns = "id", childColumns = "hoc_vien_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = {"bai_kiem_tra_id"}), @Index(value = {"hoc_vien_id"}), @Index(value = {"updated_at"})})
public class Diem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "bai_kiem_tra_id")
    public long baiKiemTraId;

    @ColumnInfo(name = "hoc_vien_id")
    public long hocVienId;

    @ColumnInfo(name = "diem_so")
    public Float diemSo;

    @ColumnInfo(name = "ghi_chu")
    public String ghiChu;

    @ColumnInfo(name = "ngay_cham")
    public String ngayCham; // yyyy-MM-dd

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
