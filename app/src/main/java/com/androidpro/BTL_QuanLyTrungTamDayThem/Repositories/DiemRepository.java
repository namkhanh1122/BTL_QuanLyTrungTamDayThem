package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.DiemDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Diem;
import java.util.List;

public class DiemRepository {
    private final DiemDao dao;

    public DiemRepository(Context ctx) { this.dao = AppDb.get(ctx).diemDao(); }

    public LiveData<List<Diem>> getByBaiKiemTraLive(long bktId){ return dao.getByBaiKiemTraLive(bktId); }
    public Diem getUnique(long bktId, long hvId){ return dao.getUnique(bktId, hvId); }
    public Diem getById(long id){ return dao.getById(id); }

    public long upsert(Diem d){
        d.updatedAt = System.currentTimeMillis();
        d.dirty = true; d.deleted = false;
        return dao.upsert(d);
    }

    public void softDelete(long id){
        Diem d = dao.getById(id);
        if (d != null){
            d.deleted = true; d.dirty = true; d.updatedAt = System.currentTimeMillis();
            dao.upsert(d);
        }
    }

    public List<Diem> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
