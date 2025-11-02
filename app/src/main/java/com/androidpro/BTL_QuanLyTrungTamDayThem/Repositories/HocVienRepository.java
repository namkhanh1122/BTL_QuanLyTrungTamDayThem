package com.androidpro.BTL_QuanLyTrungTamDayThem.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Database.AppDb;
import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.HocVienDao;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.HocVien;
import java.util.List;

public class HocVienRepository {
    private final HocVienDao dao;

    public HocVienRepository(Context ctx) { this.dao = AppDb.get(ctx).hocVienDao(); }

    public LiveData<List<HocVien>> getAllLive() { return dao.getAllLive(); }
    public HocVien getById(long id) { return dao.getById(id); }

    public long upsert(HocVien hv) {
        hv.updatedAt = System.currentTimeMillis();
        hv.dirty = true; hv.deleted = false;
        return dao.upsert(hv);
    }

    public void softDelete(long id) {
        HocVien hv = dao.getById(id);
        if (hv != null) {
            hv.deleted = true; hv.dirty = true; hv.updatedAt = System.currentTimeMillis();
            dao.upsert(hv);
        }
    }

    public List<HocVien> getDirty() { return dao.getDirty(); }
    public void markClean(List<Long> ids) { dao.markClean(ids); }
}
