package com.androidpro.BTL_QuanLyTrungTamDayThem.DAO;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.DAO.Course.CourseDAO;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;

@Database(
        entities = { Course.class },
        version = 4, // TĂNG version để phá DB cũ và chạy seed mới
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CourseDAO courseDAO();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(SEED)  // seed an toàn theo schema thật
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback SEED = new RoomDatabase.Callback() {
        @Override public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            final String table = "tblCourse";
            final long now = System.currentTimeMillis();

            // Kiểm tra schema thật của bảng
            boolean hasName  = hasColumn(db, table, "name");
            boolean hasTitle = hasColumn(db, table, "title");

            db.beginTransaction();
            try {
                if (hasName) {
                    // Schema hiện tại của bạn: Course extends BaseEntity, có field name
                    db.execSQL("INSERT INTO " + table + " (name) VALUES " +
                            "('Kotlin cho người mới bắt đầu')," +
                            "('Android Room & LiveData')," +
                            "('Jetpack Compose căn bản')," +
                            "('Clean Architecture thực chiến')");
                } else if (hasTitle) {
                    // Trường hợp dự án khác dùng title
                    db.execSQL("INSERT INTO " + table + " (title) VALUES " +
                            "('Kotlin cho người mới bắt đầu')," +
                            "('Android Room & LiveData')," +
                            "('Jetpack Compose căn bản')," +
                            "('Clean Architecture thực chiến')");
                } else {
                    // Không tìm thấy cột hợp lệ => bỏ qua seed để tránh crash
                    // (Bạn có thể log hoặc thêm cột nếu muốn)
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    };

    private static boolean hasColumn(SupportSQLiteDatabase db, String table, String column) {
        Cursor c = null;
        try {
            c = db.query("PRAGMA table_info(`" + table + "`)"); // cid, name, type, notnull, dflt_value, pk
            if (c != null) {
                int nameIdx = c.getColumnIndex("name");
                while (c.moveToNext()) {
                    if (nameIdx >= 0 && column.equalsIgnoreCase(c.getString(nameIdx))) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            if (c != null) c.close();
        }
    }
}
