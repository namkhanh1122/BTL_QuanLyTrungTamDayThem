package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean actionsAllowed = true;
    public void setActionsAllowed(boolean allowed) {
        this.actionsAllowed = allowed;
        notifyDataSetChanged();
    }

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

        h.tvStudentName.setText(item.getName());

        h.checkboxAttendance.setEnabled(this.actionsAllowed);
        h.checkboxAttendance.setChecked(item.isPresent());

        h.etGrade.setEnabled(this.actionsAllowed);
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

        // Commit grade when IME Done is pressed
        h.etGrade.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (gradeListener != null) {
                    String newGrade = h.etGrade.getText().toString();
                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(newGrade);
                    } catch (NumberFormatException e) {
                        parsed = 0;
                    }
                    if (parsed < 0 || parsed > 10) {
                        h.etGrade.setError("Điểm trong khoảng 0 - 10");
                        return false;
                    }
                    item.setScore(parsed);
                    gradeListener.onChange(item, parsed);
                }
                // clear focus and hide keyboard
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        // Fallback: also commit when focus is lost
        h.etGrade.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (gradeListener != null) {
                    String newGrade = h.etGrade.getText().toString();
                    double parsed = 0;
                    try {
                        parsed = Double.parseDouble(newGrade);
                    } catch (NumberFormatException e) {
                        parsed = 0;
                    }
                    item.setScore(parsed);
                    gradeListener.onChange(item, parsed);
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
