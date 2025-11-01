package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.DiemDanh;
import java.util.List;

@Dao
public interface DiemDanhDao {
    @Query("SELECT * FROM diem_danh WHERE deleted = 0 AND lop_id = :lopId AND ngay = :ngay ORDER BY hoc_vien_id")
    LiveData<List<DiemDanh>> getByLopNgayLive(long lopId, String ngay);

    @Query("SELECT * FROM diem_danh WHERE lop_id = :lopId AND hoc_vien_id = :hvId AND ngay = :ngay LIMIT 1")
    DiemDanh getUnique(long lopId, long hvId, String ngay);

    @Query("SELECT * FROM diem_danh WHERE id = :id LIMIT 1")
    DiemDanh getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(DiemDanh dd);

    @Query("SELECT * FROM diem_danh WHERE dirty = 1")
    List<DiemDanh> getDirty();

    @Query("UPDATE diem_danh SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
