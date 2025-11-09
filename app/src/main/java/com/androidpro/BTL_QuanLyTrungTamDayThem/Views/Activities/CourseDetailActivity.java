package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.LessonAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityCourseDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CourseDetailActivity extends BaseActivity {
    private ActivityCourseDetailBinding binding;
    private LessonAdapter lessonAdapter;
    private String courseId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getStringExtra("course_id");
        if (courseId == null || courseId.isEmpty()) {
            viewModel.sendNotification("Lỗi: Không tìm thấy ID khoá học");
            finish();
            return;
        }

        lessonAdapter = new LessonAdapter(lesson -> {
            Intent intent = new Intent(this, LessonDetailActivity.class);
            intent.putExtra("lesson_id", lesson.getId());
            startActivity(intent);
        });

        binding.rvLessons.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLessons.setAdapter(lessonAdapter);

    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);


    }

    @Override
    public void loadEvents() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        ((CourseViewModel)viewModel).loadCourseDetails(courseId);
        ((CourseViewModel)viewModel).loadLessonsForCourse(courseId);
        ((CourseViewModel)viewModel).loadStudentForCourse(courseId);

        binding.tvAddLessonLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLessonActivity.class);
            intent.putExtra("course_id", courseId);
            startActivity(intent);
        });

        binding.tvManageStudentsLink.setOnClickListener(v -> {

        });
    }

    @Override
    public void observeData() {
        super.observeData();

        ((CourseViewModel)viewModel).courseDetails.observe(this, course -> {
            if (course != null) {
                binding.tvCourseName.setText(course.getName());

                if(course.getBeginTime() != null) {
                    binding.tvBeginTime.setText(dateFormat.format(course.getBeginTime()));
                }
                if(course.getEndTime() != null) {
                    binding.tvEndTime.setText(dateFormat.format(course.getEndTime()));
                }
            }
        });

        ((CourseViewModel)viewModel).lessonList.observe(this, lessons -> {
            if (lessons != null) {
                lessonAdapter.submitList(lessons);
            }
        });

        ((CourseViewModel)viewModel).studentList.observe(this, students -> {
            if (students != null) {
                binding.tvStudents.setText(String.valueOf(students.size()));
            }
        });
    }
}
