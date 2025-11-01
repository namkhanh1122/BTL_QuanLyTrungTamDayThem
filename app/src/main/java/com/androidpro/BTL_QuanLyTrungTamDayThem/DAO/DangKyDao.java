package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.DangKy;
import java.util.List;

@Dao
public interface DangKyDao {
    @Query("SELECT * FROM dang_ky WHERE deleted = 0 AND lop_id = :lopId")
    LiveData<List<DangKy>> getByLopLive(long lopId);

    @Query("SELECT * FROM dang_ky WHERE deleted = 0 AND hoc_vien_id = :hvId")
    LiveData<List<DangKy>> getByHocVienLive(long hvId);

    @Query("SELECT * FROM dang_ky WHERE lop_id = :lopId AND hoc_vien_id = :hvId LIMIT 1")
    DangKy getUnique(long lopId, long hvId);

    @Query("SELECT * FROM dang_ky WHERE id = :id LIMIT 1")
    DangKy getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(DangKy dk);

    @Query("SELECT * FROM dang_ky WHERE dirty = 1")
    List<DangKy> getDirty();

    @Query("UPDATE dang_ky SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
