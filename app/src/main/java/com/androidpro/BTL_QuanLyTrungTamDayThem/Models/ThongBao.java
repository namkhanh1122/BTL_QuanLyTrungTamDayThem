package com.androidpro.BTL_QuanLyTrungTamDayThem.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "thong_bao",
        foreignKeys = @ForeignKey(entity = LopHoc.class, parentColumns = "id", childColumns = "lop_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"lop_id"}), @Index(value = {"updated_at"})})
public class ThongBao {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "lop_id")
    public long lopId;

    @ColumnInfo(name = "tieu_de")
    public String tieuDe;

    @ColumnInfo(name = "noi_dung")
    public String noiDung;

    @ColumnInfo(name = "tao_luc")
    public long taoLuc;

    @ColumnInfo(name = "sua_luc")
    public Long suaLuc;

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
