package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivitySimpleTitleBinding;

public class DocumentActivity extends AppCompatActivity {
    private ActivitySimpleTitleBinding b;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySimpleTitleBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Quản lý tài liệu");
        b.tvTitle.setText("Tài liệu - " + getIntent().getStringExtra("course_name"));
    }
}
