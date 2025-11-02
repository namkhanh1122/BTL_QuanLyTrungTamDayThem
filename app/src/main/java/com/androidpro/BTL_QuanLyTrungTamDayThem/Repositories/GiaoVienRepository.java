package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.GiaoVienDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.GiaoVien;
import java.util.List;

public class GiaoVienRepository {
    private final GiaoVienDao dao;

    public GiaoVienRepository(Context ctx) { this.dao = AppDb.get(ctx).giaoVienDao(); }

    public LiveData<List<GiaoVien>> getAllLive(){ return dao.getAllLive(); }
    public GiaoVien getById(long id){ return dao.getById(id); }

    public long upsert(GiaoVien gv){
        gv.updatedAt = System.currentTimeMillis();
        gv.dirty = true; gv.deleted = false;
        return dao.upsert(gv);
    }

    public void softDelete(long id){
        GiaoVien gv = dao.getById(id);
        if (gv != null){
            gv.deleted = true; gv.dirty = true; gv.updatedAt = System.currentTimeMillis();
            dao.upsert(gv);
        }
    }

    public List<GiaoVien> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
