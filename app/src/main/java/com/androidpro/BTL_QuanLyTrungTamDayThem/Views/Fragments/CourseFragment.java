package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.CourseAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentCourseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CourseFragment extends BaseFragment {
    private CourseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCourseBinding.inflate(inflater, container, false);
        super.onCreateView(inflater,container, savedInstanceState);


        return rootView;
    }

    @Override
    public void initUI() {
        adapter = new CourseAdapter((Course c) -> {
            if (c == null) return;
            startActivity(new Intent(requireContext(), CourseDetailActivity.class)
                    .putExtra("course_name", c.getName()));
        });

        ((FragmentCourseBinding)binding).rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((FragmentCourseBinding)binding).rvCourses.setAdapter(adapter);
        ((FragmentCourseBinding)binding).rvCourses.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        ((FragmentCourseBinding)binding).swipeRefresh.setOnRefreshListener(() ->  ((FragmentCourseBinding)binding).swipeRefresh.setRefreshing(false));
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    @Override
    public void observeData() {
        ((CourseViewModel)viewModel).lopHocList.observe(getViewLifecycleOwner(), courses -> {
            if (adapter != null)
            {
                adapter.submitList(courses);
                viewModel.sendNotification("Có " + courses.size() + " lớp học");
            }
        });
    }

    @Override
    public void loadEvents() {

        ((CourseViewModel)viewModel).loadCoursesRealtime();

        ((FragmentCourseBinding)binding).btnAdd.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Course course = new Course(
                        "Toán Nâng Cao Lớp 10",
                        "Khóa học rèn luyện kỹ năng giải bài nâng cao, chuẩn bị thi HSG.",
                        new Date(),
                        sdf.parse("2025-11-10 08:00:00"),
                        sdf.parse("2026-01-15 10:00:00"),
                        ScheduleStatus.Planned,
                        currentUser.getUid());

                viewModel.sendNotification("Thêm " + course.getName());
                ((CourseViewModel)viewModel).addCourse(course);
            }
            catch (Exception e) {

            }
        });
    }
}
