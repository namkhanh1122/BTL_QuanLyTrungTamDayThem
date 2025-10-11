package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentSettingsBinding;

public class SettingsFragment extends BaseFragment {

    TextView tvName, tvEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        super.onCreateView(inflater,container, savedInstanceState);


        return rootView;
    }

    @Override
    public void initUI() {
        tvName = rootView.findViewById(R.id.tvName);
        tvEmail = rootView.findViewById(R.id.tvEmail);


    }

    @Override
    public void initViewModel() {

    }

    @Override
    public void loadEvents() {

    }

    @Override
    public void observeData() {

    }
}