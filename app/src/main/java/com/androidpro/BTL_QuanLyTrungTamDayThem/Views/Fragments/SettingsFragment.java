package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseFragment;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.SettingViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities.LoginActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsFragment extends BaseFragment {
    Button btnLogout;
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
        btnLogout = rootView.findViewById(R.id.btnLogout);

        tvEmail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        tvName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(SettingViewModel.class);
    }

    @Override
    public void loadEvents() {
        btnLogout.setOnClickListener(v -> ((SettingViewModel)viewModel).logoutFirebase());

    }

    @Override
    public void observeData() {
        super.observeData();

        ((SettingViewModel) viewModel).isLoggedOut.observe(getViewLifecycleOwner(), loggedOut -> {
            if (loggedOut) {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}