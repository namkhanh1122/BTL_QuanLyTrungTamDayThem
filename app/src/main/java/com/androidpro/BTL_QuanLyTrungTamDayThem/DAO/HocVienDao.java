package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.HocVien;
import java.util.List;

@Dao
public interface HocVienDao {
    @Query("SELECT * FROM hoc_vien WHERE deleted = 0 ORDER BY ho_ten")
    LiveData<List<HocVien>> getAllLive();

    @Query("SELECT * FROM hoc_vien WHERE id = :id LIMIT 1")
    HocVien getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(HocVien hv);

    @Update
    int update(HocVien hv);

    @Query("SELECT * FROM hoc_vien WHERE dirty = 1")
    List<HocVien> getDirty();

    @Query("UPDATE hoc_vien SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
