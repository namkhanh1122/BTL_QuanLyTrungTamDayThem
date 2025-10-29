package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityCourseDetailBinding;

public class CourseDetailActivity extends AppCompatActivity {

    private ActivityCourseDetailBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        String courseName = getIntent().getStringExtra("course_name");
        setTitle(courseName != null ? courseName : "Lớp học");

        b.cardDocuments.setOnClickListener(v ->
                startActivity(new Intent(this, DocumentActivity.class)
                        .putExtra("course_name", courseName)));

        b.cardAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceActivity.class)
                        .putExtra("course_name", courseName)));

        b.cardAlerts.setOnClickListener(v ->
                startActivity(new Intent(this, AlertsActivity.class)
                        .putExtra("course_name", courseName)));

        b.cardGrades.setOnClickListener(v ->
                startActivity(new Intent(this, GradeManagementActivity.class)
                        .putExtra("course_name", courseName)));
    }
}
