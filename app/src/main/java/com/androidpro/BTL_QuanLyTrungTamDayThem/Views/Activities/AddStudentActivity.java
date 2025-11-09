package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityAddStudentBinding;

public class AddStudentActivity extends BaseActivity {
    private ActivityAddStudentBinding binding;
    private MenuItem saveItem;

    private String courseId;
    private boolean isEditMode = false;
    private String studentIdToEdit = null;
    private Student currentStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getStringExtra("course_id");
        studentIdToEdit = getIntent().getStringExtra("student_id_to_edit");
        isEditMode = (studentIdToEdit != null);

        if (isEditMode) binding.toolbar.setTitle("Sửa học viên");
        else binding.toolbar.setTitle("Thêm học viên mới");

        Menu menu = binding.toolbar.getMenu();
        saveItem = menu.findItem(R.id.action_save);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        saveItem.setOnMenuItemClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            if (name.isEmpty()) {
                binding.tilName.setError("Tên học viên là bắt buộc");
                return false;
            }

            if (isEditMode && currentStudent != null) {
                currentStudent.setName(name);
                ((CourseViewModel)viewModel).updateStudent(currentStudent);
            } else {
                Student s = new Student(name);
                ((CourseViewModel)viewModel).addStudent(courseId, s);
            }

            finish();
            return true;
        });

        if (isEditMode) {
            ((CourseViewModel)viewModel).loadStudentForCourse(courseId);
        }
    }

    @Override
    public void observeData() {
        super.observeData();

        if (isEditMode) {
            ((CourseViewModel)viewModel).studentList.observe(this, students -> {
                if (students == null) return;
                for (Student s : students) {
                    if (s.getId() != null && s.getId().equals(studentIdToEdit)) {
                        currentStudent = s;
                        binding.etName.setText(s.getName());
                        break;
                    }
                }
            });
        }
    }
}
