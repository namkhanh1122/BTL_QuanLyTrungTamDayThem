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
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LopHoc;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.CourseAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentCourseBinding;

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
        // Adapter + click
        adapter = new CourseAdapter((Course c) -> {
            if (c == null) return;
            startActivity(new Intent(requireContext(), CourseDetailActivity.class)
                    .putExtra("course_name", c.getName()));
        });

        // RecyclerView
        ((FragmentCourseBinding)binding).rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((FragmentCourseBinding)binding).rvCourses.setAdapter(adapter);
        ((FragmentCourseBinding)binding).rvCourses.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        // Pull-to-refresh
        ((FragmentCourseBinding)binding).swipeRefresh.setOnRefreshListener(() ->  ((FragmentCourseBinding)binding).swipeRefresh.setRefreshing(false));
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
    }

    @Override
    public void observeData() {
        ((CourseViewModel)viewModel).lopHocList.observe(getViewLifecycleOwner(), courses -> {
            if (adapter != null)
            {
                viewModel.sendNotification("Có " + courses.size() + " lớp học");
            }
        });
    }

    @Override
    public void loadEvents() {
        ((FragmentCourseBinding)binding).btnAdd.setOnClickListener(v -> {
            LopHoc lopHoc = new LopHoc();
            lopHoc.capLop = "Lớp 1";
            lopHoc.tenLop = "Lớp 1";
            lopHoc.monHoc = "Lớp 1";
            lopHoc.moTa = "Lớp 1";
            ((CourseViewModel)viewModel).addLopHoc(lopHoc);
        });
    }
}
