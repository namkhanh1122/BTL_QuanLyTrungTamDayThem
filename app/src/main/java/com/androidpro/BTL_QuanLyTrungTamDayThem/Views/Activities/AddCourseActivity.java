package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityAddCourseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCourseActivity extends BaseActivity {
    private ActivityAddCourseBinding binding;
    private ScheduleStatus selectedStatus;
    private Date selectedBeginTime;
    private Date selectedEndTime;
    private MenuItem saveItem;

    private String courseId;
    private Course currentCourse;
    private boolean isEditMode = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getStringExtra("course_id_to_edit");
        isEditMode = (courseId != null);
        if (isEditMode) {
            binding.toolbar.setTitle("Sửa khóa học");
        }else {
            binding.toolbar.setTitle("Thêm khóa học mới");
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
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        saveItem.setOnMenuItemClickListener(v -> {
            if (!validateInput()) {
                return false;
            }

            String name = binding.etName.getText().toString().trim();
            String description = binding.etDescription.getText().toString().trim();
            Date createAt = new Date();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (isEditMode && currentCourse != null) {
                currentCourse.setName(name); // Giả sử BaseEntity có setName
                currentCourse.setDescription(description);
                currentCourse.setBeginTime(selectedBeginTime);
                currentCourse.setEndTime(selectedEndTime);
                currentCourse.setStatus(selectedStatus);
                ((CourseViewModel)viewModel).updateCourse(currentCourse);
                finish();
                return true;
            }

            Course newCourse = new Course(
                    name,
                    description,
                    createAt,
                    selectedBeginTime,
                    selectedEndTime,
                    selectedStatus,
                    user.getUid()
            );

            ((CourseViewModel)viewModel).addCourse(newCourse);
            finish();
            return true;
        });

        binding.acvStatus.setOnItemClickListener((parent, view, position, id) -> {
            binding.tilStatus.setError(null);
            selectedStatus = ScheduleStatus.values()[position];
        });

        binding.etBeginTime.setOnClickListener(v -> showDatePickerDialog(true));

        binding.etEndTime.setOnClickListener(v -> showDatePickerDialog(false));

        ((CourseViewModel)viewModel).loadCourseDetails(courseId);
    }

    @Override
    public void observeData() {
        super.observeData();

        ((CourseViewModel)viewModel).courseDetails.observe(this, course -> {
            if (course != null) {
                currentCourse = course;
                binding.etName.setText(course.getName());
                binding.etDescription.setText(course.getDescription());

                if(course.getBeginTime() != null) {
                    selectedBeginTime = course.getBeginTime();
                    binding.etBeginTime.setText(dateFormat.format(course.getBeginTime()));
                }

                if(course.getEndTime() != null) {
                    selectedEndTime = course.getEndTime();
                    binding.etEndTime.setText(dateFormat.format(course.getEndTime()));
                }

                if(course.getStatus() != null) {
                    selectedStatus = course.getStatus();
                    binding.acvStatus.setText(course.getStatus().toString(), false);
                }
            }
        });
    }

    private void showDatePickerDialog(boolean isBeginTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date selectedDate = calendar.getTime();
            String formattedDate = dateFormat.format(selectedDate);

            if (isBeginTime) {
                binding.etBeginTime.setError(null);
                selectedBeginTime = selectedDate;
                binding.etBeginTime.setText(formattedDate);
            } else {

                if (selectedBeginTime != null && selectedBeginTime.after(selectedDate)) {
                    binding.tilEndTime.setError("Ngày kết thúc phải sau ngày bắt đầu");
                    return;
                }
                else
                    binding.tilEndTime.setError(null);


                selectedEndTime = selectedDate;
                binding.etEndTime.setText(formattedDate);
            }
        };

        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInput() {
        boolean valid = true;
        if (binding.etName.getText().toString().trim().isEmpty()) {
            binding.tilName.setError("Tên khóa học là bắt buộc");
            valid = false;
        } else {
            binding.tilName.setError(null);
        }
        if (selectedStatus == null) {
            binding.tilStatus.setError("Vui lòng chọn trạng thái");
            valid = false;
        } else {
            binding.tilStatus.setError(null);
        }
        if (selectedBeginTime == null) {
            binding.tilBeginTime.setError("Vui lòng chọn ngày");
            valid = false;
        } else {
            binding.tilBeginTime.setError(null);
        }
        if (selectedEndTime == null) {
            binding.tilEndTime.setError("Vui lòng chọn ngày");
            valid = false;
        } else {
            binding.tilEndTime.setError(null);
        }
        if (selectedBeginTime != null && selectedEndTime != null && selectedEndTime.before(selectedBeginTime)) {
            binding.tilEndTime.setError("Ngày kết thúc phải sau ngày bắt đầu");
            valid = false;
        }
        return valid;
    }
}