package com.androidpro.BTL_QuanLyTrungTamDayThem.Database;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Converters.DateConverter;
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
                            .addCallback(SEED) // seed khi DB tạo lần đầu
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ===== Seed dữ liệu mẫu (chạy một lần khi DB vừa tạo) =====
    private static final Callback SEED = new Callback() {
        @Override public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            final long now = System.currentTimeMillis();

            // Tên bảng đúng theo @Entity(tableName = "..."):
            final String gvTable  = "giao_vien";
            final String lhTable  = "lop_hoc";
            final String hvTable  = "hoc_vien";
            final String dkTable  = "dang_ky";
            final String ldTable  = "lich_day";
            final String bktTable = "bai_kiem_tra";
            final String diemTable= "diem";
            final String ddTable  = "diem_danh";
            final String tlTable  = "tai_lieu";
            final String tbTable  = "thong_bao";

            db.beginTransaction();
            try {
                // ========= GIAO_VIEN =========
                // Cột: ho_ten, email, so_dien_thoai, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + gvTable +
                        " (ho_ten, email, so_dien_thoai, remote_id, updated_at, deleted, dirty) VALUES " +
                        "('Nguyễn Minh An', 'an.nguyen@gv.edu', '0901000001', NULL, " + now + ", 0, 0)," +
                        "('Trần Thu Hà', 'ha.tran@gv.edu', '0901000002', NULL, " + now + ", 0, 0)," +
                        "('Lê Quốc Bảo', 'bao.le@gv.edu', '0901000003', NULL, " + now + ", 0, 0)," +
                        "('Phạm Bích Ngọc', 'ngoc.pham@gv.edu', '0901000004', NULL, " + now + ", 0, 0)," +
                        "('Đỗ Hải Long', 'long.do@gv.edu', '0901000005', NULL, " + now + ", 0, 0)");

                // ========= LOP_HOC =========
                // Cột: ten_lop, mon_hoc, cap_lop, mo_ta, giao_vien_id, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + lhTable +
                        " (ten_lop, mon_hoc, cap_lop, mo_ta, giao_vien_id, remote_id, updated_at, deleted, dirty) VALUES " +
                        "('Toán 12 Nâng cao', 'Toán', '12', 'Ôn luyện nâng cao cho kỳ thi THPT', 1, NULL, " + now + ", 0, 0)," +
                        "('Ngữ văn 11 Căn bản', 'Ngữ văn', '11', 'Củng cố kỹ năng làm văn và đọc hiểu', 2, NULL, " + now + ", 0, 0)," +
                        "('Hóa 12 Ôn thi', 'Hóa học', '12', 'Este, lipit và các chuyên đề trọng tâm', 3, NULL, " + now + ", 0, 0)," +
                        "('Tiếng Anh Giao tiếp A2', 'Tiếng Anh', 'A2', 'Giao tiếp cơ bản chủ đề đời sống', 4, NULL, " + now + ", 0, 0)");

                // ========= HOC_VIEN =========
                // Cột: ho_ten, ma_hoc_vien, email, so_dien_thoai, phu_huynh, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + hvTable +
                        " (ho_ten, ma_hoc_vien, email, so_dien_thoai, phu_huynh, remote_id, updated_at, deleted, dirty) VALUES " +
                        "('Phạm Gia Huy', 'HV001', 'huy.pham@hv.edu', '0912000001', 'Phạm Hải', NULL, " + now + ", 0, 0)," +
                        "('Ngô Minh Châu', 'HV002', 'chau.ngo@hv.edu', '0912000002', 'Ngô Lan', NULL, " + now + ", 0, 0)," +
                        "('Vũ Khánh Linh', 'HV003', 'linh.vu@hv.edu', '0912000003', 'Vũ Hưng', NULL, " + now + ", 0, 0)," +
                        "('Trần Đức Anh', 'HV004', 'anh.tran@hv.edu', '0912000004', 'Trần Thành', NULL, " + now + ", 0, 0)," +
                        "('Lý Mỹ Duyên', 'HV005', 'duyen.ly@hv.edu', '0912000005', 'Lý Quân', NULL, " + now + ", 0, 0)," +
                        "('Nguyễn Quỳnh Mai', 'HV006', 'mai.nguyen@hv.edu', '0912000006', 'Nguyễn Hòa', NULL, " + now + ", 0, 0)");

                // ========= DANG_KY =========
                // Cột: lop_id, hoc_vien_id, ngay_tham_gia, trang_thai, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + dkTable +
                        " (lop_id, hoc_vien_id, ngay_tham_gia, trang_thai, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 1, '2025-02-03', 'dang_hoc', NULL, " + now + ", 0, 0)," +
                        "(1, 2, '2025-02-03', 'dang_hoc', NULL, " + now + ", 0, 0)," +
                        "(2, 3, '2025-02-04', 'dang_hoc', NULL, " + now + ", 0, 0)," +
                        "(3, 4, '2025-02-07', 'dang_hoc', NULL, " + now + ", 0, 0)," +
                        "(4, 5, '2025-02-09', 'dang_hoc', NULL, " + now + ", 0, 0)," +
                        "(2, 6, '2025-02-04', 'tam_dung', NULL, " + now + ", 0, 0)");

                // ========= LICH_DAY =========
                // Cột: lop_id, thu_trong_tuan, gio_bat_dau, gio_ket_thuc, phong, ghi_chu, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + ldTable +
                        " (lop_id, thu_trong_tuan, gio_bat_dau, gio_ket_thuc, phong, ghi_chu, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 3, '18:00', '19:30', 'P201', 'Chủ đề: Hàm số', NULL, " + now + ", 0, 0)," +
                        "(1, 5, '18:00', '19:30', 'P201', 'Ôn tập', NULL, " + now + ", 0, 0)," +
                        "(2, 2, '19:30', '21:00', 'P103', 'Nghị luận xã hội', NULL, " + now + ", 0, 0)," +
                        "(3, 6, '18:00', '20:00', 'P305', 'Este - Lipit', NULL, " + now + ", 0, 0)," +
                        "(4, 7, '09:00', '11:00', 'P402', 'Speaking: Daily life', NULL, " + now + ", 0, 0)");

                // ========= BAI_KIEM_TRA =========
                // Cột: lop_id, ten_bai, he_so, diem_toi_da, han_nop, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + bktTable +
                        " (lop_id, ten_bai, he_so, diem_toi_da, han_nop, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 'KT Chương 1 - Toán 12', 1.0, 10.0, '2025-02-15', NULL, " + now + ", 0, 0)," + // id=1
                        "(2, 'Viết đoạn văn 200 chữ', 1.0, 10.0, '2025-02-16', NULL, " + now + ", 0, 0)," + // id=2
                        "(3, 'Hóa 12 - Este', 1.0, 10.0, '2025-02-17', NULL, " + now + ", 0, 0)");          // id=3

                // ========= DIEM =========
                // Cột: bai_kiem_tra_id, hoc_vien_id, diem_so, ghi_chu, ngay_cham, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + diemTable +
                        " (bai_kiem_tra_id, hoc_vien_id, diem_so, ghi_chu, ngay_cham, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 1, 8.5, 'Làm tốt phần đạo hàm', '2025-02-16', NULL, " + now + ", 0, 0)," +
                        "(1, 2, 7.8, 'Cần luyện bài cực trị', '2025-02-16', NULL, " + now + ", 0, 0)," +
                        "(2, 3, 8.2, 'Diễn đạt mạch lạc', '2025-02-17', NULL, " + now + ", 0, 0)," +
                        "(2, 6, 7.0, 'Thiếu dẫn chứng', '2025-02-17', NULL, " + now + ", 0, 0)," +
                        "(3, 4, 8.9, 'Hiểu bản chất phản ứng', '2025-02-18', NULL, " + now + ", 0, 0)");

                // ========= DIEM_DANH =========
                // Cột: lop_id, hoc_vien_id, ngay, trang_thai, ghi_chu, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + ddTable +
                        " (lop_id, hoc_vien_id, ngay, trang_thai, ghi_chu, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 1, '2025-02-03', 'co_mat', '', NULL, " + now + ", 0, 0)," +
                        "(1, 2, '2025-02-03', 'co_mat', '', NULL, " + now + ", 0, 0)," +
                        "(2, 3, '2025-02-04', 'co_mat', '', NULL, " + now + ", 0, 0)," +
                        "(2, 6, '2025-02-04', 'phep', 'Báo trước', NULL, " + now + ", 0, 0)," +
                        "(3, 4, '2025-02-07', 'co_mat', '', NULL, " + now + ", 0, 0)");

                // ========= TAI_LIEU =========
                // Cột: lop_id, tieu_de, ten_file, mime_type, kich_thuoc, storage_url, duong_dan_cuc_bo, ghi_chu, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + tlTable +
                        " (lop_id, tieu_de, ten_file, mime_type, kich_thuoc, storage_url, duong_dan_cuc_bo, ghi_chu, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 'Tổng hợp công thức đạo hàm', 'dao-ham.pdf', 'application/pdf', 245760, 'https://example.com/dao-ham.pdf', NULL, 'PDF 8 trang', NULL, " + now + ", 0, 0)," +
                        "(2, 'Dàn ý nghị luận xã hội', 'dan-y.pdf', 'application/pdf', 163840, 'https://example.com/dan-y.pdf', NULL, 'Mẫu dàn ý tham khảo', NULL, " + now + ", 0, 0)," +
                        "(4, 'Từ vựng giao tiếp A2', 'vocab-a2.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 102400, 'https://example.com/vocab-a2.docx', NULL, 'Danh sách ~300 từ', NULL, " + now + ", 0, 0)");

                // ========= THONG_BAO =========
                // Cột: lop_id, tieu_de, noi_dung, tao_luc, sua_luc, remote_id, updated_at, deleted, dirty
                db.execSQL("INSERT INTO " + tbTable +
                        " (lop_id, tieu_de, noi_dung, tao_luc, sua_luc, remote_id, updated_at, deleted, dirty) VALUES " +
                        "(1, 'Lịch kiểm tra Chương 1', 'Lớp Toán 12 kiểm tra ngày 15/02, ôn kỹ đạo hàm.', " + now + ", NULL, NULL, " + now + ", 0, 0)," +
                        "(2, 'Bài viết tuần này', 'Viết đoạn văn 200 chữ về chủ đề đã cho, nộp trước buổi học.', " + now + ", NULL, NULL, " + now + ", 0, 0)," +
                        "(4, 'File từ vựng mới', 'Đã up danh sách từ vựng A2, xem trước nhé.', " + now + ", NULL, NULL, " + now + ", 0, 0)");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        // (Không còn cần preferTable vì tên bảng đã cố định theo @Entity)
        @SuppressWarnings("unused")
        private boolean exists(SupportSQLiteDatabase db, String table) {
            Cursor c = null;
            try {
                c = db.query("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                        new Object[]{table});
                return c != null && c.moveToFirst();
            } finally {
                if (c != null) c.close();
            }
        }
    };
}
