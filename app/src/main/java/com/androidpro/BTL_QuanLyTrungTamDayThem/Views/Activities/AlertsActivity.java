package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivitySimpleTitleBinding;

public class AlertsActivity extends AppCompatActivity {
    private ActivitySimpleTitleBinding b;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySimpleTitleBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Thông báo lớp");
        b.tvTitle.setText("Thông báo - " + getIntent().getStringExtra("course_name"));
    }
}
