package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.BaiKiemTraDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.BaiKiemTra;
import java.util.List;

public class BaiKiemTraRepository {
    private final BaiKiemTraDao dao;

    public BaiKiemTraRepository(Context ctx) { this.dao = AppDb.get(ctx).baiKiemTraDao(); }

    public LiveData<List<BaiKiemTra>> getByLopLive(long lopId){ return dao.getByLopLive(lopId); }
    public BaiKiemTra getById(long id){ return dao.getById(id); }

    public long upsert(BaiKiemTra bkt){
        bkt.updatedAt = System.currentTimeMillis();
        bkt.dirty = true; bkt.deleted = false;
        return dao.upsert(bkt);
    }

    public void softDelete(long id){
        BaiKiemTra bkt = dao.getById(id);
        if (bkt != null){
            bkt.deleted = true; bkt.dirty = true; bkt.updatedAt = System.currentTimeMillis();
            dao.upsert(bkt);
        }
    }

    public List<BaiKiemTra> getDirty(){ return dao.getDirty(); }
    public void markClean(List<Long> ids){ dao.markClean(ids); }
}
