package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "dang_ky",
        foreignKeys = {
                @ForeignKey(entity = LopHoc.class, parentColumns = "id", childColumns = "lop_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = HocVien.class, parentColumns = "id", childColumns = "hoc_vien_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = {"lop_id"}), @Index(value = {"hoc_vien_id"}), @Index(value = {"updated_at"})})
public class DangKy {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "lop_id")
    public long lopId;

    @ColumnInfo(name = "hoc_vien_id")
    public long hocVienId;

    @ColumnInfo(name = "ngay_tham_gia")
    public String ngayThamGia; // yyyy-MM-dd

    @ColumnInfo(name = "trang_thai")
    public String trangThai; // dang_hoc/nghi/tam_dung

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
