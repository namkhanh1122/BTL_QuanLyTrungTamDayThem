package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.VH> {
    public interface OnItemClick {
        void onClick(Student student);
    }

    private final List<Student> data = new ArrayList<>();
    private final OnItemClick listener;

    public StudentAdapter(OnItemClick listener) {
        this.listener = listener;
    }

    public void submitList(List<Student> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    public Student getStudentAt(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public StudentAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_management, parent, false);
        return new StudentAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.VH h, int position) {
        Student s = data.get(position);
        h.tvStudentName.setText(s.getName());

        // show placeholders while loading
        h.tvAverageGrade.setText("0.0");
        h.tvAttendanceRatio.setText("0 / 0");

        FirebaseRepository.getInstance().getAttendancesForStudent(s.getId(), new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Attendance> attendances) {
                int currentPos = h.getBindingAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return;
                Student currentlyBound = null;
                if (currentPos < data.size()) currentlyBound = data.get(currentPos);
                if (currentlyBound == null || !currentlyBound.getId().equals(s.getId())) return;

                if (attendances == null || attendances.isEmpty()) {
                    h.tvAverageGrade.setText("0.0");
                    h.tvAttendanceRatio.setText("0 / 0");
                    return;
                }

                double sum = 0;
                int scoredCount = 0;
                int presentCount = 0;
                for (Attendance a : attendances) {
                    if (a.isPresent()) presentCount++;
                    if (a.getScore() > 0) {
                        sum += a.getScore();
                        scoredCount++;
                    }
                }
                double avg = 0.0;
                if (scoredCount > 0) avg = sum / scoredCount;

                h.tvAverageGrade.setText(String.format(Locale.getDefault(), "%.1f", avg));
                h.tvAttendanceRatio.setText(presentCount + " / " + attendances.size());
            }

            @Override
            public void onError(String error) {
                // keep placeholders on error; consider logging
                int currentPos = h.getBindingAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return;
                Student currentlyBound = null;
                if (currentPos < data.size()) currentlyBound = data.get(currentPos);
                if (currentlyBound == null || !currentlyBound.getId().equals(s.getId())) return;
                h.tvAverageGrade.setText("0.0");
                h.tvAttendanceRatio.setText("0 / 0");
            }
        });


        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(s);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextView tvAverageGrade;
        TextView tvAttendanceRatio;
        VH(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvAverageGrade = itemView.findViewById(R.id.tvAverageGrade);
            tvAttendanceRatio = itemView.findViewById(R.id.tvAttendanceRatio);
        }
    }

    // no extra Stats class — adapter computes values from Attendance
}
