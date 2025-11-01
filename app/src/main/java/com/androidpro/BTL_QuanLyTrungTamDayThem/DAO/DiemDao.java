package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Diem;
import java.util.List;

@Dao
public interface DiemDao {
    @Query("SELECT * FROM diem WHERE deleted = 0 AND bai_kiem_tra_id = :bktId ORDER BY hoc_vien_id")
    LiveData<List<Diem>> getByBaiKiemTraLive(long bktId);

    @Query("SELECT * FROM diem WHERE bai_kiem_tra_id = :bktId AND hoc_vien_id = :hvId LIMIT 1")
    Diem getUnique(long bktId, long hvId);

    @Query("SELECT * FROM diem WHERE id = :id LIMIT 1")
    Diem getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(Diem d);

    @Query("SELECT * FROM diem WHERE dirty = 1")
    List<Diem> getDirty();

    @Query("UPDATE diem SET dirty = 0 WHERE id IN (:ids)")
    void markClean(List<Long> ids);
}
