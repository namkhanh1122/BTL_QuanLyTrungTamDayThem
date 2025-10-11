package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements ViewInitializable {
    protected NotifiableViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initViewModel();
        loadEvents();
        observeData();
    }


    @Override
    public void observeData() {
        viewModel.getNotifyMessage().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
