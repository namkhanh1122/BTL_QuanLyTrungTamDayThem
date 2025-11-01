package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseListViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments.adapters.CourseAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentCourseBinding;

public class CourseFragment extends BaseFragment {

    private FragmentCourseBinding b;
    private CourseListViewModel vm;
    private CourseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentCourseBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Theo khuôn BaseFragment: tách nhỏ và gọi tuần tự
        initViewModel();
        initUI();
        observeData();
        loadEvents();
    }

    // ====== Các hàm bắt buộc của BaseFragment/ViewInitializable ======

    @Override
    public void initUI() {
        // Adapter + click
        adapter = new CourseAdapter((Course c) -> {
            if (c == null) return;
            startActivity(new Intent(requireContext(), CourseDetailActivity.class)
                    .putExtra("course_name", c.getName()));
        });

        // RecyclerView
        b.rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.rvCourses.setAdapter(adapter);
        b.rvCourses.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        // Pull-to-refresh
        b.swipeRefresh.setOnRefreshListener(() -> b.swipeRefresh.setRefreshing(false));
    }

    @Override
    public void initViewModel() {
        vm = new ViewModelProvider(this).get(CourseListViewModel.class);
    }

    @Override
    public void observeData() {
        vm.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (adapter != null) adapter.submitList(courses);
        });
    }

    @Override
    public void loadEvents() {
        // Chưa có sự kiện bổ sung
    }
}
