package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LessonViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.AttendanceAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.DocumentActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityLessonDetailBinding;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LessonDetailActivity extends BaseActivity {

    private ActivityLessonDetailBinding binding;
    private String lessonId;;

    private AttendanceAdapter attendanceAdapter;
    private boolean actionsAllowed = true;

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

        attendanceAdapter = new AttendanceAdapter((attendance, isChecked) -> {
            // prevent changes when course is completed/canceled
            if (!actionsAllowed) {
                if (viewModel != null) viewModel.sendNotification("Không thể sửa điểm danh: khoá học đã kết thúc hoặc bị hủy");
                // refresh adapter to revert UI state
                attendanceAdapter.notifyDataSetChanged();
                return;
            }

            // Update attendance present flag and persist
            attendance.setPresent(isChecked);
            if (viewModel instanceof LessonViewModel) {
                ((LessonViewModel) viewModel).updateAttendance(attendance);
            }
        }, (attendance, score) -> {
            if (!actionsAllowed) {
                if (viewModel != null) viewModel.sendNotification("Không thể sửa điểm: khoá học đã kết thúc hoặc bị hủy");
                attendanceAdapter.notifyDataSetChanged();
                return;
            }

            // Update attendance score and persist
            attendance.setScore(score);
            if (viewModel instanceof LessonViewModel) {
                ((LessonViewModel) viewModel).updateAttendance(attendance);
            }
        });

        binding.rvStudentAttendance.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudentAttendance.setAdapter(attendanceAdapter);

        // open document manager when tapping documents card (pass actions_allowed flag)
        binding.cardDocuments.setOnClickListener(v -> {
            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("lesson_id", lessonId);
            intent.putExtra("lesson_title", binding.tvLessonTitle.getText().toString());
            intent.putExtra("actions_allowed", actionsAllowed);
            startActivity(intent);
        });
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        if (lessonId == null || lessonId.isEmpty()) {
            viewModel.sendNotification("Lỗi: Không tìm thấy ID buổi học");
            finish();
            return;
        }

        ((LessonViewModel)viewModel).loadLessonDetails(lessonId);
        // Load attendance records for this lesson
        ((LessonViewModel)viewModel).loadAttendanceForLesson(lessonId);
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

                if (lesson.getStatus() != null) {
                    ScheduleStatus st = lesson.getStatus();
                    actionsAllowed = st == ScheduleStatus.Active;
                } else {
                    actionsAllowed = true;
                }
                attendanceAdapter.setActionsAllowed(actionsAllowed);
            }
        });

        // Observe attendance list and submit to adapter
        ((LessonViewModel)viewModel).attendanceList.observe(this, attendances -> {
            if (attendances != null) {
                attendanceAdapter.submitList(attendances);
            }
        });
    }
}