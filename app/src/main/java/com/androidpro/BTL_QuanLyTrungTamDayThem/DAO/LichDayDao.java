package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LichDay;
import java.util.List;

@Dao
public interface LichDayDao {
    @Query("SELECT * FROM lich_day WHERE deleted = 0 AND lop_id = :lopId ORDER BY thu_trong_tuan, gio_bat_dau")
    LiveData<List<LichDay>> getByLopLive(long lopId);

    @Query("SELECT * FROM lich_day WHERE id = :id LIMIT 1")
    LichDay getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(LichDay ld);

    @Query("SELECT * FROM lich_day WHERE dirty = 1")
    List<LichDay> getDirty();

    @Query("UPDATE lich_day SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
