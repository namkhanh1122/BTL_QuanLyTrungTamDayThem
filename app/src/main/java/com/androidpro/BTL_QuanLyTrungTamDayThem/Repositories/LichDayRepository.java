package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.LichDayDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LichDay;
import java.util.List;

public class LichDayRepository {
    private final LichDayDao dao;

    public LichDayRepository(Context ctx) { this.dao = AppDb.get(ctx).lichDayDao(); }

    public LiveData<List<LichDay>> getByLopLive(long lopId){ return dao.getByLopLive(lopId); }

    public long upsert(LichDay ld){
        ld.updatedAt = System.currentTimeMillis();
        ld.dirty = true; ld.deleted = false;
        return dao.upsert(ld);
    }

    public void softDelete(long id){
        LichDay ld = dao.getById(id);
        if (ld != null){
            ld.deleted = true; ld.dirty = true; ld.updatedAt = System.currentTimeMillis();
            dao.upsert(ld);
        }
    }

    public List<LichDay> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
