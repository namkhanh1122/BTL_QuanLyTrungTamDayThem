package com.androidpro.BTL_QuanLyTrungTamDayThem.Workers;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

// *** IMPORT REPOSITORY ***
import androidx.core.content.ContextCompat;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ScheduleWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Lesson> lessonsList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, HH:mm dd/MM/yyyy", new Locale("vi", "VN"));

    // --- THAY ĐỔI: Sử dụng Repository ---
    private final FirebaseRepository repository;

    public ScheduleWidgetFactory(Context context, Intent intent) {
        mContext = context;
        // Khởi tạo repository
        repository = FirebaseRepository.getInstance();
    }

    @Override
    public void onCreate() {
        // Khởi tạo
    }

    @Override
    public void onDataSetChanged() {
        Log.d("WidgetFactory", "onDataSetChanged: Đang tải dữ liệu từ Repository...");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("WidgetFactory", "Chưa đăng nhập");
            lessonsList.clear();
            return;
        }

        String uid = currentUser.getUid();
        List<Lesson> tempLessons = new ArrayList<>();

        try {
            QuerySnapshot coursesSnapshot = Tasks.await(
                    repository.getCoursesForInstructor_Task(uid)
            );

            if (coursesSnapshot.isEmpty()) {
                Log.d("WidgetFactory", "Không có khóa học nào.");
                lessonsList.clear();
                return;
            }

            for (DocumentSnapshot courseDoc : coursesSnapshot.getDocuments()) {
                String courseId = courseDoc.getId();
                Log.d("WidgetFactory", "Đang lấy lesson cho course: " + courseId);

                QuerySnapshot lessonsSnapshot = Tasks.await(
                        repository.getFutureLessonsInCourse_Task(courseId)
                );

                if (!lessonsSnapshot.isEmpty()) {
                    tempLessons.addAll(lessonsSnapshot.toObjects(Lesson.class));
                }
            }

            tempLessons.sort(Comparator.comparing(Lesson::getBeginTime));

            lessonsList.clear();
            lessonsList.addAll(tempLessons);
            Log.d("WidgetFactory", "Tải thành công qua Repository: " + lessonsList.size() + " buổi học.");

        } catch (ExecutionException | InterruptedException e) {
            Log.e("WidgetFactory", "Lỗi tải dữ liệu đồng bộ: " + e.getMessage());
            Thread.currentThread().interrupt();
            lessonsList.clear();
        }
    }

    @Override
    public void onDestroy() {
        lessonsList.clear();
    }

    @Override
    public int getCount() {
        return lessonsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= lessonsList.size()) {
            return null;
        }

        Lesson lesson = lessonsList.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_schedule_widget);

        views.setTextViewText(R.id.widget_item_title, lesson.getTitle());
        if (lesson.getBeginTime() != null) {
            views.setTextViewText(R.id.widget_item_time, dateFormat.format(lesson.getBeginTime()));
        } else {
            views.setTextViewText(R.id.widget_item_time, "N/A");
        }

        String label;
        int bgResId; // ID của file drawable nền
        int textColor; // Màu chữ

        if (lesson.getStatus() != null) {
            switch (lesson.getStatus()) {
                case Active:
                    label = "Đang";
                    // LỖI CỦA BẠN: Phải dùng drawable của màu XANH LÁ
                    bgResId = R.drawable.widget_badge_bg_active;
                    textColor = Color.WHITE;
                    break;
                case Completed:
                    label = "Hoàn thành";
                    // LỖI CỦA BẠN: Phải dùng drawable của màu XANH DƯƠNG
                    bgResId = R.drawable.widget_badge_bg_completed;
                    textColor = Color.WHITE;
                    break;
                case Canceled:
                    label = "Đã hủy";
                    // LỖI CỦA BẠN: Phải dùng drawable của màu ĐỎ
                    bgResId = R.drawable.widget_badge_bg_canceled;
                    textColor = Color.WHITE;
                    break;
                default: // Planned
                    label = "Sắp";
                    // LỖI CỦA BẠN: Phải dùng drawable của màu XÁM
                    bgResId = R.drawable.widget_badge_bg_planned;
                    // LỖI CỦA BẠN: Phải dùng màu chữ cho planned
                    textColor = ContextCompat.getColor(mContext, R.color.widget_badge_bg);
            }
        } else {
            // Fallback phòng trường hợp status là null
            label = "Sắp";
            bgResId = R.drawable.widget_badge_bg_planned;
            textColor = ContextCompat.getColor(mContext, R.color.widget_badge_bg);
        }
        views.setTextViewText(R.id.widget_item_status, label);

        views.setTextColor(R.id.widget_item_status, textColor);

        views.setInt(R.id.widget_item_status, "setBackgroundResource", bgResId);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("lesson_id", lesson.getId());

        views.setOnClickFillInIntent(R.id.widget_item_root, fillInIntent);

        return views;
    }

    // ... (Các hàm còn lại giữ nguyên: getLoadingView, getViewTypeCount, getItemId, hasStableIds) ...
    @Override
    public RemoteViews getLoadingView() { return null; }
    @Override
    public int getViewTypeCount() { return 1; }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public boolean hasStableIds() { return false; }
}