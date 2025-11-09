package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.VH>{

    public interface OnAttendanceChangeListener {
        void onChange(Attendance attendance, boolean isChecked);
    }

    public interface OnGradeChangeListener {
        void onChange(Attendance attendance, double grade);
    }

    private final List<Attendance> data = new ArrayList<>();

    private final OnAttendanceChangeListener attendanceListener;
    private final OnGradeChangeListener gradeListener;

    public AttendanceAdapter(OnAttendanceChangeListener attendanceListener, OnGradeChangeListener gradeListener) {
        this.attendanceListener = attendanceListener;
        this.gradeListener = gradeListener;
    }

    public void submitList(List<Attendance> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);
        return new AttendanceAdapter.VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.VH h, int position) {
        Attendance item = data.get(position);

        // 1. Gán dữ liệu
        h.tvStudentName.setText(item.getName());

        h.checkboxAttendance.setOnCheckedChangeListener(null);
        h.checkboxAttendance.setChecked(item.isPresent());

        // 3. Gán dữ liệu (và xóa listener cũ)
        // (Chúng ta dùng onFocusChange thay vì TextWatcher cho đơn giản)
        if (item.getScore() > 0) {
            h.etGrade.setText(String.valueOf(item.getScore()));
        } else {
            h.etGrade.setText("");
        }

        h.checkboxAttendance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (attendanceListener != null) {
                item.setPresent(isChecked);
                attendanceListener.onChange(item, isChecked);
            }
        });

        h.etGrade.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (gradeListener != null) {
                    String newGrade = h.etGrade.getText().toString();

                    try {
                        item.setScore(Double.parseDouble(newGrade));
                    } catch (NumberFormatException e) {
                        item.setScore(0);
                    }
                    gradeListener.onChange(item, Double.parseDouble(newGrade));
                }
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextInputEditText etGrade;
        CheckBox checkboxAttendance;

        VH(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            etGrade = itemView.findViewById(R.id.etGrade);
            checkboxAttendance = itemView.findViewById(R.id.checkboxAttendance);
        }
    }
}
