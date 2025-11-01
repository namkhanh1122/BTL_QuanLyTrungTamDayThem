package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.ThongBao;
import java.util.List;

@Dao
public interface ThongBaoDao {
    @Query("SELECT * FROM thong_bao WHERE deleted = 0 AND lop_id = :lopId ORDER BY tao_luc DESC")
    LiveData<List<ThongBao>> getByLopLive(long lopId);

    @Query("SELECT * FROM thong_bao WHERE id = :id LIMIT 1")
    ThongBao getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(ThongBao tb);

    @Query("SELECT * FROM thong_bao WHERE dirty = 1")
    List<ThongBao> getDirty();

    @Query("UPDATE thong_bao SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
