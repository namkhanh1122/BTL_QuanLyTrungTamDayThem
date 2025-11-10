package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.HomeViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LessonViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.AddLessonActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.CourseDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.LessonDetailActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.LessonAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;

public class HomeFragment extends BaseFragment {
    private LessonAdapter lessonAdapter;
    private LessonViewModel lessonViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        super.onCreateView(inflater,container, savedInstanceState);

        return rootView;
    }

    @Override
    public void initUI() {
        lessonAdapter = new LessonAdapter(lesson -> {
            Intent intent = new Intent(requireContext(), LessonDetailActivity.class);
            intent.putExtra("lesson_id", lesson.getId());
            startActivity(intent);
        });

        ((FragmentHomeBinding)binding).rvSchedules.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((FragmentHomeBinding)binding).rvSchedules.setAdapter(lessonAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson lesson = lessonAdapter.getLessonAt(position);

                if (direction == ItemTouchHelper.LEFT) {
                    // Mark lesson as Canceled instead of deleting
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Hủy buổi học")
                            .setMessage("Bạn có muốn hủy buổi học \"" + lesson.getTitle() + "\" không?")
                            .setPositiveButton("Đồng ý", (dialog, which) -> {
                                final ScheduleStatus prev = lesson.getStatus();
                                lesson.setStatus(ScheduleStatus.Canceled);
                                // persist change
                                if (lessonViewModel != null) lessonViewModel.updateLesson(lesson);
                                // update UI immediately
                                lessonAdapter.notifyItemChanged(position);

                                Snackbar.make(((FragmentHomeBinding) binding).rvSchedules, "Đã hủy " + lesson.getTitle(), Snackbar.LENGTH_LONG)
                                        .setAction("Hoàn tác", v -> {
                                            lesson.setStatus(prev);
                                            if (lessonViewModel != null) lessonViewModel.updateLesson(lesson);
                                            lessonAdapter.notifyItemChanged(position);
                                        })
                                        .show();
                            })
                            .setNegativeButton("Hủy", (dialog, which) -> {
                                lessonAdapter.notifyItemChanged(position);
                            })
                            .setOnCancelListener(dialog -> lessonAdapter.notifyItemChanged(position))
                            .show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
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
                        background.setColor(Color.parseColor("#f39c12"));

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
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(((FragmentHomeBinding)binding).rvSchedules);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);
    }

    @Override
    public void loadEvents() {
        ((HomeViewModel)viewModel).loadLessonsRealtime();

        ((FragmentHomeBinding)binding).swipeRefreshLayout.setOnRefreshListener(() -> {
            ((HomeViewModel)viewModel).loadLessonsRealtime();
        });
    }

    @Override
    public void observeData() {
        super.observeData();

        ((HomeViewModel)viewModel).allLessons.observe(getViewLifecycleOwner(), lessons -> {
            ((FragmentHomeBinding)binding).swipeRefreshLayout.setRefreshing(false);
            if (lessons != null) {

                lessons.sort(Comparator.comparing(Lesson::getBeginTime));

                lessonAdapter.submitList(lessons);
            }
        });
    }
}