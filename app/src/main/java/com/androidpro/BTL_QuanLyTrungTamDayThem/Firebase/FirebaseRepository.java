package com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase;

import androidx.annotation.NonNull;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {
    private final  FirebaseDatabase database = FirebaseDatabase.getInstance("https://qltrungtamdaythem-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference courseRef;

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public FirebaseRepository() {
        courseRef = database.getReference("Courses");
    }

    public void addCourses(Course course, DataCallback<Void> callback) {
        String id = courseRef.push().getKey();

        course.setId(id);

        courseRef.child(String.valueOf(course.getId())).setValue(course)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void listenCourses(DataCallback<List<Course>> callback) {
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Course> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Course c = s.getValue(Course.class);
                    if (c != null) list.add(c);
                }
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }
}
