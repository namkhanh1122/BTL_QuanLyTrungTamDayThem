package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseListViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentCourseBinding;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments.adapters.CourseAdapter;

public class CourseFragment extends BaseFragment {

    private FragmentCourseBinding b;
    private CourseListViewModel vm;
    private CourseAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        b = FragmentCourseBinding.inflate(getLayoutInflater());
        binding = b;
        rootView = b.getRoot();

        adapter = new CourseAdapter(course -> {
            Intent i = new Intent(requireContext(), CourseDetailActivity.class);
            i.putExtra("course_id", course.getId());
            i.putExtra("course_name", course.getName());
            startActivity(i);
        });

        b.rvCourses.setAdapter(adapter);
        b.rvCourses.addItemDecoration(
                new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        );

        b.swipeRefresh.setOnRefreshListener(() -> {
            // dữ liệu là LiveData nên tự cập nhật; chỉ cần tắt refresh icon
            b.swipeRefresh.setRefreshing(false);
        });

        // Nút thêm (tuỳ sau này bạn xử lý)
        b.fabAdd.setOnClickListener(v -> {
            // TODO: mở dialog tạo lớp mới
        });
    }

    @Override
    public void initViewModel() {
        vm = new ViewModelProvider(this).get(CourseListViewModel.class);
    }

    @Override
    public void loadEvents() { }

    @Override
    public void observeData() {
        vm.getCourses().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
