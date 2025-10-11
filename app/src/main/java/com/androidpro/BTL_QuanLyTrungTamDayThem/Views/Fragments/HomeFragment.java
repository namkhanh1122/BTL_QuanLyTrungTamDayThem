package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentHomeBinding;

public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        super.onCreateView(inflater,container, savedInstanceState);

        return rootView;
    }

    @Override
    public void initUI() {

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