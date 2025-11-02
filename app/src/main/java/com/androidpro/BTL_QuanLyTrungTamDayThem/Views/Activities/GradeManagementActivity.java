package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivitySimpleTitleBinding;

public class GradeManagementActivity extends AppCompatActivity {
    private ActivitySimpleTitleBinding b;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySimpleTitleBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Quản lý điểm");
        b.tvTitle.setText("Quản lý điểm - " + getIntent().getStringExtra("course_name"));
    }
}
