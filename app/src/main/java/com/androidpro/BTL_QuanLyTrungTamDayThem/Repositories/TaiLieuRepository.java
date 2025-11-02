package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.TaiLieuDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.TaiLieu;
import java.util.List;

public class TaiLieuRepository {
    private final TaiLieuDao dao;

    public TaiLieuRepository(Context ctx) { this.dao = AppDb.get(ctx).taiLieuDao(); }

    public LiveData<List<TaiLieu>> getByLopLive(long lopId){ return dao.getByLopLive(lopId); }
    public TaiLieu getById(long id){ return dao.getById(id); }

    public long upsert(TaiLieu tl){
        tl.updatedAt = System.currentTimeMillis();
        tl.dirty = true; tl.deleted = false;
        return dao.upsert(tl);
    }

    public void softDelete(long id){
        TaiLieu tl = dao.getById(id);
        if (tl != null){
            tl.deleted = true; tl.dirty = true; tl.updatedAt = System.currentTimeMillis();
            dao.upsert(tl);
        }
    }

    public List<TaiLieu> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
