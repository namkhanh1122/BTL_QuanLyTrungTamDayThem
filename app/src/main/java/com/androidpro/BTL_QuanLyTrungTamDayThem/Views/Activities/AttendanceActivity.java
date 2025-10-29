package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivitySimpleTitleBinding;

public class AttendanceActivity extends AppCompatActivity {
    private ActivitySimpleTitleBinding b;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySimpleTitleBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Điểm danh");
        b.tvTitle.setText("Điểm danh - " + getIntent().getStringExtra("course_name"));
    }
}
