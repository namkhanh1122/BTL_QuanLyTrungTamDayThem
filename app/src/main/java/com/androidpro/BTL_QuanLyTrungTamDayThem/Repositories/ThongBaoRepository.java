package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.ThongBaoDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.ThongBao;
import java.util.List;

public class ThongBaoRepository {
    private final ThongBaoDao dao;

    public ThongBaoRepository(Context ctx) { this.dao = AppDb.get(ctx).thongBaoDao(); }

    public LiveData<List<ThongBao>> getByLopLive(long lopId){ return dao.getByLopLive(lopId); }
    public ThongBao getById(long id){ return dao.getById(id); }

    public long upsert(ThongBao tb){
        long now = System.currentTimeMillis();
        tb.updatedAt = now; tb.dirty = true; tb.deleted = false;
        if (tb.taoLuc == 0) tb.taoLuc = now;
        return dao.upsert(tb);
    }

    public void softDelete(long id){
        ThongBao tb = dao.getById(id);
        if (tb != null){
            tb.deleted = true; tb.dirty = true; tb.updatedAt = System.currentTimeMillis();
            dao.upsert(tb);
        }
    }

    public List<ThongBao> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
