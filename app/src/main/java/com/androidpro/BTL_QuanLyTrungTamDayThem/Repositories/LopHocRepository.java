package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.LopHocDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LopHoc;
import java.util.List;

public class LopHocRepository {
    private final LopHocDao dao;

    public LopHocRepository(Context ctx) { this.dao = AppDb.get(ctx).lopHocDao(); }

    public LiveData<List<LopHoc>> getAllLive() { return dao.getAllLive(); }
    public LopHoc getById(long id) { return dao.getById(id); }

    public long upsert(LopHoc lh) {
        lh.updatedAt = System.currentTimeMillis();
        lh.dirty = true; lh.deleted = false;
        return dao.upsert(lh);
    }

    public void softDelete(long id) {
        LopHoc lh = dao.getById(id);
        if (lh != null) {
            lh.deleted = true; lh.dirty = true; lh.updatedAt = System.currentTimeMillis();
            dao.upsert(lh);
        }
    }

    public List<LopHoc> getDirty() { return dao.getDirty(); }
    public void markClean(List<Long> ids) { dao.markClean(ids); }
}
