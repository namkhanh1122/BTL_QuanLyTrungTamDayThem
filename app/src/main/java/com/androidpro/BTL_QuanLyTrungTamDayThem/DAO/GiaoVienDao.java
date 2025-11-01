package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.GiaoVien;
import java.util.List;

@Dao
public interface GiaoVienDao {
    @Query("SELECT * FROM giao_vien WHERE deleted = 0 ORDER BY ho_ten")
    LiveData<List<GiaoVien>> getAllLive();

    @Query("SELECT * FROM giao_vien WHERE id = :id LIMIT 1")
    GiaoVien getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(GiaoVien gv);

    @Query("SELECT * FROM giao_vien WHERE dirty = 1")
    List<GiaoVien> getDirty();

    @Query("UPDATE giao_vien SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
