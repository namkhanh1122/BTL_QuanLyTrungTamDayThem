package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginViewModel extends BaseViewModel {
    FirebaseAuth mAuth;

    private final MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    public void loginFirebase(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null)
                            firebaseUser.postValue(task.getResult().getUser());
                        else
                            firebaseUser.postValue(null);

                    });
        } catch (Exception e) {
            Log.e("FIREBASE", "Login Fail", e);
        }
    }

}
