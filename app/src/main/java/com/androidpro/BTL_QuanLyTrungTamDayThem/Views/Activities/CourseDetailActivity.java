package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.LessonAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityCourseDetailBinding;

import com.google.android.material.snackbar.Snackbar;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LessonViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CourseDetailActivity extends BaseActivity {
    private ActivityCourseDetailBinding binding;
    private LessonAdapter lessonAdapter;
    private String courseId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private LessonViewModel lessonViewModel;

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

        // Add swipe actions (left = delete with undo, right = edit)
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
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
                    new AlertDialog.Builder(CourseDetailActivity.this)
                            .setTitle("Hủy buổi học")
                            .setMessage("Bạn có muốn hủy buổi học \"" + lesson.getTitle() + "\" không?")
                            .setPositiveButton("Đồng ý", (dialog, which) -> {
                                final ScheduleStatus prev = lesson.getStatus();
                                lesson.setStatus(ScheduleStatus.Canceled);
                                // persist change
                                if (lessonViewModel != null) lessonViewModel.updateLesson(lesson);
                                // update UI immediately
                                lessonAdapter.notifyItemChanged(position);

                                Snackbar.make(binding.rvLessons, "Đã hủy " + lesson.getTitle(), Snackbar.LENGTH_LONG)
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
                else if (direction == ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(CourseDetailActivity.this, AddLessonActivity.class);
                    intent.putExtra("lesson_id_to_edit", lesson.getId());
                    intent.putExtra("course_id", courseId);
                    startActivity(intent);

                    lessonAdapter.notifyItemChanged(position);
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
                        icon = ContextCompat.getDrawable(CourseDetailActivity.this, R.drawable.ic_edit_24);
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
                        icon = ContextCompat.getDrawable(CourseDetailActivity.this, R.drawable.ic_delete_24);
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
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rvLessons);

    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);
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
            Intent intent = new Intent(this, StudentManagementActivity.class);
            intent.putExtra("course_id", courseId);
            startActivity(intent);
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
