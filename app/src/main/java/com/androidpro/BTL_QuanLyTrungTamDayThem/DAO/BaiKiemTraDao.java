package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.BaiKiemTra;
import java.util.List;

@Dao
public interface BaiKiemTraDao {
    @Query("SELECT * FROM bai_kiem_tra WHERE deleted = 0 AND lop_id = :lopId ORDER BY id DESC")
    LiveData<List<BaiKiemTra>> getByLopLive(long lopId);

    @Query("SELECT * FROM bai_kiem_tra WHERE id = :id LIMIT 1")
    BaiKiemTra getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(BaiKiemTra bkt);

    @Query("SELECT * FROM bai_kiem_tra WHERE dirty = 1")
    List<BaiKiemTra> getDirty();

    @Query("UPDATE bai_kiem_tra SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
