package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.DiemDanhDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.DiemDanh;
import java.util.List;

public class DiemDanhRepository {
    private final DiemDanhDao dao;

    public DiemDanhRepository(Context ctx) { this.dao = AppDb.get(ctx).diemDanhDao(); }

    public LiveData<List<DiemDanh>> getByLopNgayLive(long lopId, String ngay){
        return dao.getByLopNgayLive(lopId, ngay);
    }

    public DiemDanh getUnique(long lopId, long hvId, String ngay){
        return dao.getUnique(lopId, hvId, ngay);
    }

    public long upsert(DiemDanh dd){
        dd.updatedAt = System.currentTimeMillis();
        dd.dirty = true; dd.deleted = false;
        return dao.upsert(dd);
    }

    public void softDelete(long id){
        DiemDanh dd = dao.getById(id);
        if (dd != null){
            dd.deleted = true; dd.dirty = true; dd.updatedAt = System.currentTimeMillis();
            dao.upsert(dd);
        }
    }

    public List<DiemDanh> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
