package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LopHoc;
import java.util.List;

@Dao
public interface LopHocDao {
    @Query("SELECT * FROM lop_hoc WHERE deleted = 0 ORDER BY ten_lop")
    LiveData<List<LopHoc>> getAllLive();

    @Query("SELECT * FROM lop_hoc WHERE id = :id LIMIT 1")
    LopHoc getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(LopHoc lh);

    @Update
    int update(LopHoc lh);

    @Query("SELECT * FROM lop_hoc WHERE dirty = 1")
    List<LopHoc> getDirty();

    @Query("UPDATE lop_hoc SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
