package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.TaiLieu;
import java.util.List;

@Dao
public interface TaiLieuDao {
    @Query("SELECT * FROM tai_lieu WHERE deleted = 0 AND lop_id = :lopId ORDER BY updated_at DESC")
    LiveData<List<TaiLieu>> getByLopLive(long lopId);

    @Query("SELECT * FROM tai_lieu WHERE id = :id LIMIT 1")
    TaiLieu getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(TaiLieu tl);

    @Query("SELECT * FROM tai_lieu WHERE dirty = 1")
    List<TaiLieu> getDirty();

    @Query("UPDATE tai_lieu SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
