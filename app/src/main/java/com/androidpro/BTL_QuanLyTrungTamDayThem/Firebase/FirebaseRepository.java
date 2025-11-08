package com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.LopHoc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {
    private final DatabaseReference lophocRef;

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public FirebaseRepository() {
        lophocRef = FirebaseDatabase.getInstance("https://qltrungtamdaythem-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LopHoc");
    }

    public void addLopHoc(LopHoc lopHoc, DataCallback<Void> callback) {
        lophocRef.child(String.valueOf(lopHoc.id)).setValue(lopHoc)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getAllLopHoc(DataCallback<List<LopHoc>> callback) {
        lophocRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                List<LopHoc> lopHocList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LopHoc lopHoc = snapshot.getValue(LopHoc.class);
                    lopHocList.add(lopHoc);
                }
                callback.onSuccess(lopHocList);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
}
