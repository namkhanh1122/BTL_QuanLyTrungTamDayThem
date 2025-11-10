package com.androidpro.BTL_QuanLyTrungTamDayThem.Core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment extends Fragment implements ViewInitializable {
    protected View rootView;
    protected ViewBinding binding;

    protected NotifiableViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = binding.getRoot();

        initUI();
        initViewModel();
        observeData();
        loadEvents();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void observeData() {
        viewModel.getNotifyMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
    }
}
