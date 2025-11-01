package com.androidpro.BTL_QuanLyTrungTamDayThem.Database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.*;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.*;

@Database(
        entities = {
                GiaoVien.class, LopHoc.class, HocVien.class, DangKy.class,
                LichDay.class, DiemDanh.class, ThongBao.class, TaiLieu.class,
                BaiKiemTra.class, Diem.class
        },
        version = 1,
        exportSchema = true
)
public abstract class AppDb extends RoomDatabase {

    private static volatile AppDb INSTANCE;

    // DAO getters
    public abstract GiaoVienDao giaoVienDao();
    public abstract LopHocDao lopHocDao();
    public abstract HocVienDao hocVienDao();
    public abstract DangKyDao dangKyDao();
    public abstract LichDayDao lichDayDao();
    public abstract DiemDanhDao diemDanhDao();
    public abstract ThongBaoDao thongBaoDao();
    public abstract TaiLieuDao taiLieuDao();
    public abstract BaiKiemTraDao baiKiemTraDao();
    public abstract DiemDao diemDao();

    public static AppDb get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    AppDb.class, "ql_trung_tam.db")
                            .addCallback(SEED)
                            .fallbackToDestructiveMigration() // dev: cho phép reset khi đổi version
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Seed dữ liệu mẫu (tuỳ chọn)
    private static final Callback SEED = new Callback() {
        @Override public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Có thể insert mẫu qua db.execSQL(...) nếu muốn
        }
    };
}
