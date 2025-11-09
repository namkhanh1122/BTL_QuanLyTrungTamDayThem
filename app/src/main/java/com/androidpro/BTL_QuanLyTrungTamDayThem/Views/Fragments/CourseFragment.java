package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.AddCourseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.CourseAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentCourseBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends BaseFragment {
    private SearchView searchView;
    private CourseAdapter adapter;
    private FragmentCourseBinding fragmentBinding;
    private List<Course> allCourses = new ArrayList<>();
    private String currentSearchQuery = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCourseBinding.inflate(inflater, container, false);

        fragmentBinding = (FragmentCourseBinding) binding;

        super.onCreateView(inflater,container, savedInstanceState);


        return rootView;
    }

    @Override
    public void initUI() {

        Menu menu = fragmentBinding.toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        adapter = new CourseAdapter((Course c) -> {
            if (c == null) return;
            startActivity(new Intent(requireContext(), CourseDetailActivity.class)
                    .putExtra("course_id", c.getId()));
        });

        fragmentBinding.rvCourses.setLayoutManager(new LinearLayoutManager(requireContext()));
        fragmentBinding.rvCourses.setAdapter(adapter);

        fragmentBinding.swipeRefresh.setOnRefreshListener(() -> {
            filterListByTab(fragmentBinding.tabLayout.getSelectedTabPosition());
            fragmentBinding.swipeRefresh.setRefreshing(false);
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();

                Course course = adapter.getCourseAt(position);

                if (direction == ItemTouchHelper.LEFT) {
                    ((CourseViewModel)viewModel).deleteCourse(course.getId());
                    Snackbar.make(fragmentBinding.rvCourses, "Đã xóa " + course.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Hoàn tác", v -> {
                                ((CourseViewModel)viewModel).addCourse(course);
                            })
                            .show();
                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(requireContext(), AddCourseActivity.class);
                    intent.putExtra("course_id_to_edit", course.getId());
                    startActivity(intent);

                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    Drawable icon;
                    ColorDrawable background = new ColorDrawable();

                    int iconMarginVertical = (itemView.getHeight() - 72) / 2;
                    int iconMarginHorizontal = 40;

                    if (dX > 0) {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit_24);
                        background.setColor(Color.parseColor("#3498db"));

                        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                        background.draw(c);

                        int iconTop = itemView.getTop() + iconMarginVertical;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();
                        int iconLeft = itemView.getLeft() + iconMarginHorizontal;
                        int iconRight = itemView.getLeft() + iconMarginHorizontal + icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);

                    } else if (dX < 0) {
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_24);
                        background.setColor(Color.parseColor("#e74c3c"));

                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);

                        int iconTop = itemView.getTop() + iconMarginVertical;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();
                        int iconRight = itemView.getRight() - iconMarginHorizontal;
                        int iconLeft = itemView.getRight() - iconMarginHorizontal - icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    } else {
                        background.setBounds(0, 0, 0, 0);
                        background.draw(c);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(fragmentBinding.rvCourses);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    @Override
    public void observeData() {
        ((CourseViewModel)viewModel).courseList.observe(getViewLifecycleOwner(), courses -> {
            if (adapter != null)
            {
                allCourses.clear();
                allCourses.addAll(courses);

                filterListByTab(fragmentBinding.tabLayout.getSelectedTabPosition());
            }
        });
    }

    @Override
    public void loadEvents() {

        ((CourseViewModel)viewModel).loadCoursesRealtime();

        ((FragmentCourseBinding)binding).btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddCourseActivity.class));
        });

        fragmentBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterListByTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterListByTab(fragmentBinding.tabLayout.getSelectedTabPosition());
                return true;
            }
        });
    }

    private void filterListByTab(int position) {
        if (adapter == null) return;

        List<Course> filteredList = new ArrayList<>();
        if (position == 0) {
            for (Course c : allCourses) {
                if (c.getStatus() != ScheduleStatus.Completed) {
                    filteredList.add(c);
                }
            }
        } else {
            for (Course c : allCourses) {
                if (c.getStatus() == ScheduleStatus.Completed) {
                    filteredList.add(c);
                }
            }
        }

        List<Course> nameFilteredList;

        if (currentSearchQuery.isEmpty()) {
            nameFilteredList = filteredList;
        } else {
            nameFilteredList = new ArrayList<>();
            String lowerCaseQuery = currentSearchQuery.toLowerCase().trim();

            for (Course course : filteredList) {
                if (course.getName().toLowerCase().contains(lowerCaseQuery)) {
                    nameFilteredList.add(course);
                }
            }
        }

        adapter.submitList(nameFilteredList);
    }
}
