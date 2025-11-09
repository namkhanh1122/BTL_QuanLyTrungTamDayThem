package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.CourseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.StudentAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityStudentManagementBinding;
import com.google.android.material.snackbar.Snackbar;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
// AtomicInteger removed — use simple int counter instead

public class StudentManagementActivity extends BaseActivity {

	private ActivityStudentManagementBinding binding;
	private StudentAdapter adapter;
	private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public void initUI() {
		binding = ActivityStudentManagementBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

        courseId = getIntent().getStringExtra("course_id");
        if (courseId == null || courseId.isEmpty()) {
            viewModel.sendNotification("Lỗi: Không tìm thấy ID khoá học");
            finish();
            return;
        }

		adapter = new StudentAdapter(student -> {
			Intent intent = new Intent(this, AddStudentActivity.class);
			intent.putExtra("course_id", courseId);
			intent.putExtra("student_id_to_edit", student.getId());
			startActivity(intent);
		});

		binding.rvStudents.setLayoutManager(new LinearLayoutManager(this));
		binding.rvStudents.setAdapter(adapter);
	}

	@Override
	public void initViewModel() {
		viewModel = new ViewModelProvider(this).get(CourseViewModel.class);

	}

	@Override
	public void loadEvents() {
		binding.toolbar.setNavigationOnClickListener(v -> finish());
		((CourseViewModel)viewModel).loadStudentForCourse(courseId);

		binding.fabAddStudent.setOnClickListener(v -> {
			Intent intent = new Intent(this, AddStudentActivity.class);
			intent.putExtra("course_id", courseId);
			startActivity(intent);
		});

		ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
				int position = viewHolder.getBindingAdapterPosition();
				com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student student = adapter.getStudentAt(position);

				if (direction == ItemTouchHelper.LEFT) {
					new AlertDialog.Builder(StudentManagementActivity.this)
							.setTitle("Xác nhận xóa")
							.setMessage("Bạn có chắc muốn xóa học viên \"" + student.getName() + "\" không?")
							.setPositiveButton("Xóa", (dialog, which) -> {
								((CourseViewModel) viewModel).deleteStudent(student.getId());
								Snackbar.make(binding.rvStudents, "Đã xóa " + student.getName(), Snackbar.LENGTH_LONG)
										.setAction("Hoàn tác", v -> ((CourseViewModel) viewModel).addStudent(courseId, student))
										.show();
							})
							.setNegativeButton("Hủy", (dialog, which) -> adapter.notifyItemChanged(position))
							.setOnCancelListener(dialog -> adapter.notifyItemChanged(position))
							.show();
				} else {
					// edit
					Intent intent = new Intent(StudentManagementActivity.this, AddStudentActivity.class);
					intent.putExtra("course_id", courseId);
					intent.putExtra("student_id_to_edit", student.getId());
					startActivity(intent);
					adapter.notifyItemChanged(position);
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
						icon = ContextCompat.getDrawable(StudentManagementActivity.this, R.drawable.ic_edit_24);
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
						icon = ContextCompat.getDrawable(StudentManagementActivity.this, R.drawable.ic_delete_24);
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

		new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rvStudents);
	}


	@Override
	public void observeData() {
		super.observeData();

		((CourseViewModel)viewModel).studentList.observe(this, students -> adapter.submitList(students));

	}
}