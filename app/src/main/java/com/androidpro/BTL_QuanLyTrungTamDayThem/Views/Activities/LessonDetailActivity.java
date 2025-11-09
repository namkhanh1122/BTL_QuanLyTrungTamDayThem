package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LessonViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.AttendanceAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityLessonDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LessonDetailActivity extends BaseActivity {

    private ActivityLessonDetailBinding binding;
    private String lessonId;;

    private AttendanceAdapter attendanceAdapter;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initUI() {
        binding = ActivityLessonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lessonId = getIntent().getStringExtra("lesson_id");
        if (lessonId == null || lessonId.isEmpty()) {
            viewModel.sendNotification("Lỗi: Không tìm thấy ID buổi học");
            finish();
            return;
        }

        attendanceAdapter = new AttendanceAdapter((attendance, isChecked) -> {
        }, (attendance, score) -> {
        });

        binding.rvStudentAttendance.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudentAttendance.setAdapter(attendanceAdapter);

    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        ((LessonViewModel)viewModel).loadLessonDetails(lessonId);

    }

    @Override
    public void observeData() {
        super.observeData();

        ((LessonViewModel)viewModel).lessonDetail.observe(this, lesson -> {
            if (lesson != null) {
                binding.tvLessonTitle.setText(lesson.getTitle());

                binding.tvContent.setText(lesson.getContent());

                if(lesson.getBeginTime() != null) {
                    binding.tvBeginTime.setText(dateFormat.format(lesson.getBeginTime()));
                }
                if(lesson.getEndTime() != null) {
                    binding.tvEndTime.setText(dateFormat.format(lesson.getEndTime()));
                }
            }
        });
    }
}