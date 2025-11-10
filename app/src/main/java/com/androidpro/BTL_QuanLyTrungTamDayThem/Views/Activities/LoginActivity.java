package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.LoginViewModel;

public class LoginActivity extends BaseActivity {
    Button btnLogin;
    EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public void loadEvents() {
        btnLogin.setOnClickListener(v -> {
            String email = String.valueOf(edtEmail.getText());
            String password = String.valueOf(edtPassword.getText());

            if (TextUtils.isEmpty(email)) {
                viewModel.sendNotification("Nhập tài khoản đầy đủ");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                viewModel.sendNotification("Nhập mật khẩu đầy đủ");
                return;
            }

            ((LoginViewModel)viewModel).loginFirebase(email, password);
        });
    }

    @Override
    public void observeData() {
        super.observeData();

        ((LoginViewModel)viewModel).getFirebaseUser().observe(this, user -> {
            if (user != null) {
                Intent intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", user.getDisplayName());
                bundle.putString("email", user.getEmail());

                intent.putExtras(bundle);

                startActivity(intent, bundle);
                finish();
            }
            else {
                viewModel.sendNotification("Đăng nhập không thành công");
            }
        });
    }
}