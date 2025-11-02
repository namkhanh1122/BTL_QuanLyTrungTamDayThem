package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.DangKyDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.DangKy;
import java.util.List;

public class DangKyRepository {
    private final DangKyDao dao;

    public DangKyRepository(Context ctx) { this.dao = AppDb.get(ctx).dangKyDao(); }

    public LiveData<List<DangKy>> getByLopLive(long lopId){ return dao.getByLopLive(lopId); }
    public LiveData<List<DangKy>> getByHocVienLive(long hvId){ return dao.getByHocVienLive(hvId); }
    public DangKy getUnique(long lopId, long hvId){ return dao.getUnique(lopId, hvId); }

    public long upsert(DangKy dk){
        dk.updatedAt = System.currentTimeMillis();
        dk.dirty = true; dk.deleted = false;
        return dao.upsert(dk);
    }

    public void softDelete(long id){
        DangKy dk = dao.getById(id);
        if (dk != null){
            dk.deleted = true; dk.dirty = true; dk.updatedAt = System.currentTimeMillis();
            dao.upsert(dk);
        }
    }

    public List<DangKy> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
