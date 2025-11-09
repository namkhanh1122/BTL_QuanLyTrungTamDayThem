package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LessonViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityAddLessonBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddLessonActivity extends BaseActivity {
    private ActivityAddLessonBinding binding;
    private ScheduleStatus selectedStatus;
    private Date selectedBeginTime;
    private Date selectedEndTime;
    private MenuItem saveItem;

    private String courseId;
    private boolean isEditMode = false;
    private Lesson currentLesson = null;
    private String lessonIdToEdit = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        binding = ActivityAddLessonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getStringExtra("course_id");
        lessonIdToEdit = getIntent().getStringExtra("lesson_id_to_edit");
        isEditMode = (lessonIdToEdit != null);

        if (isEditMode) {
            binding.toolbar.setTitle("Sửa buổi học");
        } else {
            binding.toolbar.setTitle("Thêm buổi học mới");
        }

        Menu menu = binding.toolbar.getMenu();
        saveItem = menu.findItem(R.id.action_save);

        List<String> statusNames = new ArrayList<>();
        for (ScheduleStatus status : ScheduleStatus.values()) {
            statusNames.add(status.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusNames);
        binding.acvStatus.setAdapter(adapter);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        ((LessonViewModel)viewModel).loadLessonsInCourseRealtime(courseId);

        if (isEditMode) {
            ((LessonViewModel)viewModel).loadLessonDetails(lessonIdToEdit);
        }

        binding.acvStatus.setOnItemClickListener((parent, view, position, id) -> {
            selectedStatus = ScheduleStatus.values()[position];
        });

        binding.etBeginTime.setOnClickListener(v -> showDatePickerDialog(true));
        binding.etEndTime.setOnClickListener(v -> showDatePickerDialog(false));

        saveItem.setOnMenuItemClickListener(v -> {
            if (!validateInput()) {
                return false;
            }

            String title = binding.etLessonTitle.getText().toString().trim();
            String content = binding.etContent.getText().toString().trim();
            String videoUrl = binding.etVideoUrl.getText().toString().trim();

            Lesson newLesson = new Lesson(
                    title,
                    content,
                    videoUrl,
                    selectedBeginTime,
                    selectedEndTime,
                    selectedStatus
            );
            if (isEditMode && currentLesson != null) {
                // update currentLesson
                currentLesson.setTitle(title);
                currentLesson.setContent(content);
                currentLesson.setVideoUrl(videoUrl);
                currentLesson.setBeginTime(selectedBeginTime);
                currentLesson.setEndTime(selectedEndTime);
                currentLesson.setStatus(selectedStatus);

                ((LessonViewModel)viewModel).updateLesson(currentLesson);
            } else {
                ((LessonViewModel)viewModel).addLesson(courseId, newLesson);
            }

            finish();
            return true;
        });

    }

    @Override
    public void observeData() {
        super.observeData();

        ((LessonViewModel)viewModel).lessonDetail.observe(this, lesson -> {
            if (lesson == null) return;
            currentLesson = lesson;

            binding.etLessonTitle.setText(lesson.getTitle());
            binding.etContent.setText(lesson.getContent());
            binding.etVideoUrl.setText(lesson.getVideoUrl());

            if (lesson.getBeginTime() != null) {
                selectedBeginTime = lesson.getBeginTime();
                binding.etBeginTime.setText(dateFormat.format(lesson.getBeginTime()));
            }

            if (lesson.getEndTime() != null) {
                selectedEndTime = lesson.getEndTime();
                binding.etEndTime.setText(dateFormat.format(lesson.getEndTime()));
            }

            if (lesson.getStatus() != null) {
                selectedStatus = lesson.getStatus();
                binding.acvStatus.setText(lesson.getStatus().toString(), false);
            }
        });

    }

    private void showDatePickerDialog(boolean isBeginTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // 3. Đã có Date đầy đủ
                Date selectedDate = calendar.getTime();

                // (Tùy chọn) Kiểm tra logic thời gian
                if (!isBeginTime && selectedBeginTime != null && selectedDate.before(selectedBeginTime)) {
                    binding.tilEndTime.setError("Thời gian kết thúc phải sau thời gian bắt đầu");
                    return;
                } else {
                    binding.tilEndTime.setError(null);
                }

                String formattedDate = dateFormat.format(selectedDate);
                if (isBeginTime) {
                    selectedBeginTime = selectedDate;
                    binding.etBeginTime.setText(formattedDate);
                } else {
                    selectedEndTime = selectedDate;
                    binding.etEndTime.setText(formattedDate);
                }
            };

            new TimePickerDialog(this, timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show(); // true = 24h format
        };

        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInput() {
        boolean valid = true;

        if (binding.etLessonTitle.getText().toString().trim().isEmpty()) {
            binding.tilLessonTitle.setError("Tiêu đề là bắt buộc");
            valid = false;
        } else {
            binding.tilLessonTitle.setError(null);
        }

        if (selectedStatus == null) {
            binding.tilStatus.setError("Vui lòng chọn trạng thái");
            valid = false;
        } else {
            binding.tilStatus.setError(null);
        }

        if (selectedBeginTime == null) {
            binding.tilBeginTime.setError("Vui lòng chọn thời gian");
            valid = false;
        } else {
            binding.tilBeginTime.setError(null);
        }

        if (selectedEndTime == null) {
            binding.tilEndTime.setError("Vui lòng chọn thời gian");
            valid = false;
        } else {
            binding.tilEndTime.setError(null);
        }

        if (selectedBeginTime != null && selectedEndTime != null && selectedEndTime.before(selectedBeginTime)) {
            binding.tilEndTime.setError("Thời gian kết thúc phải sau thời gian bắt đầu");
            valid = false;
        }
        return valid;
    }
}