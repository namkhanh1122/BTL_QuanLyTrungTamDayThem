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

        // status is computed automatically from begin/end times; no manual input
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

        // no manual status selection

        // --- THAY ĐỔI: Gọi hàm chọn thời gian bắt đầu ---
        binding.etBeginTime.setOnClickListener(v -> showBeginTimePicker());
        // --- THAY ĐỔI: Gọi hàm chọn thời gian kết thúc ---
        binding.etEndTime.setOnClickListener(v -> showEndTimePicker());

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
                    computeStatus(selectedBeginTime, selectedEndTime)
            );
            if (isEditMode && currentLesson != null) {
                // update currentLesson
                currentLesson.setTitle(title);
                currentLesson.setContent(content);
                currentLesson.setVideoUrl(videoUrl);
                currentLesson.setBeginTime(selectedBeginTime);
                currentLesson.setEndTime(selectedEndTime);
                currentLesson.setStatus(computeStatus(selectedBeginTime, selectedEndTime));

                ((LessonViewModel)viewModel).updateLesson(currentLesson);
            } else {
                ((LessonViewModel)viewModel).addLesson(newLesson);
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

            // status is computed from times; no manual display
        });

    }

    // --- THAY ĐỔI: Đổi tên hàm và chỉ dùng cho thời gian BẮT ĐẦU ---
    private void showBeginTimePicker() {
        Calendar calendar = Calendar.getInstance();

        // Nếu đã có thời gian (chế độ sửa) thì dùng thời gian đó
        if (selectedBeginTime != null) {
            calendar.setTime(selectedBeginTime);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // 3. Đã có Date đầy đủ
                selectedBeginTime = calendar.getTime();
                binding.etBeginTime.setText(dateFormat.format(selectedBeginTime));
                binding.tilBeginTime.setError(null); // Xóa lỗi nếu có

                // 4. KIỂM TRA: Nếu thời gian kết thúc cũ bị sai (trước thời gian bắt đầu mới)
                // thì xóa nó đi và bắt người dùng chọn lại
                if (selectedEndTime != null && selectedEndTime.before(selectedBeginTime)) {
                    selectedEndTime = null;
                    binding.etEndTime.setText("");
                    binding.tilEndTime.setError("Vui lòng chọn lại thời gian kết thúc");
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

    // --- THÊM MỚI: Hàm chỉ chọn giờ KẾT THÚC ---
    private void showEndTimePicker() {
        // 1. Phải chọn thời gian bắt đầu trước
        if (selectedBeginTime == null) {
            binding.tilBeginTime.setError("Vui lòng chọn thời gian bắt đầu trước");
            return;
        }
        binding.tilBeginTime.setError(null); // Xóa lỗi

        // 2. Lấy Calendar và set thời gian mặc định (là thời gian bắt đầu)
        Calendar endCalendar = Calendar.getInstance();

        // Nếu đã có thời gian kết thúc (và hợp lệ) thì dùng nó
        if (selectedEndTime != null && !selectedEndTime.before(selectedBeginTime)) {
            endCalendar.setTime(selectedEndTime);
        } else {
            // Nếu không thì lấy thời gian bắt đầu làm gợi ý
            endCalendar.setTime(selectedBeginTime);
        }

        // 3. Chỉ hiện TimePickerDialog (Không hiện DatePicker)
        TimePickerDialog.OnTimeSetListener timeSetListener = (timeView, hourOfDay, minute) -> {
            // 4. Tạo 1 Calendar mới dựa trên NGÀY của thời gian BẮT ĐẦU
            Calendar newEndCalendar = Calendar.getInstance();
            newEndCalendar.setTime(selectedBeginTime); // Quan trọng: Đặt ngày = ngày bắt đầu

            // Set giờ và phút người dùng mới chọn
            newEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            newEndCalendar.set(Calendar.MINUTE, minute);

            Date newEndDate = newEndCalendar.getTime();

            // 5. Validation: Thời gian kết thúc phải SAU thời gian bắt đầu
            if (newEndDate.before(selectedBeginTime) || newEndDate.equals(selectedBeginTime)) {
                binding.tilEndTime.setError("Thời gian kết thúc phải sau thời gian bắt đầu");
                return;
            }

            // Hợp lệ
            binding.tilEndTime.setError(null);
            selectedEndTime = newEndDate;
            binding.etEndTime.setText(dateFormat.format(selectedEndTime));
        };

        new TimePickerDialog(this, timeSetListener,
                endCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.MINUTE),
                true).show(); // true = 24h format
    }


    private boolean validateInput() {
        boolean valid = true;

        if (binding.etLessonTitle.getText().toString().trim().isEmpty()) {
            binding.tilLessonTitle.setError("Tiêu đề là bắt buộc");
            valid = false;
        } else {
            binding.tilLessonTitle.setError(null);
        }

        // status is derived automatically from begin/end times

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

        // --- SỬA ĐỔI: Kiểm tra nghiêm ngặt hơn ---
        if (selectedBeginTime != null && selectedEndTime != null) {
            if (selectedEndTime.before(selectedBeginTime) || selectedEndTime.equals(selectedBeginTime)) {
                binding.tilEndTime.setError("Thời gian kết thúc phải sau thời gian bắt đầu");
                valid = false;
            }
        }
        return valid;
    }

    private ScheduleStatus computeStatus(Date begin, Date end) {
        Date now = new Date();
        if (begin == null || end == null) return ScheduleStatus.Planned;
        if (now.before(begin)) return ScheduleStatus.Planned;
        if (!now.before(begin) && !now.after(end)) return ScheduleStatus.Active;
        if (now.after(end)) return ScheduleStatus.Completed;
        return ScheduleStatus.Planned;
    }
}