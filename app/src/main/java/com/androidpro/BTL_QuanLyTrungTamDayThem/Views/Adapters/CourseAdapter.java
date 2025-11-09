package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.mUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> {

    public interface OnItemClick {
        void onClick(Course course);
    }

    private final List<Course> data = new ArrayList<>();
    private final OnItemClick listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public CourseAdapter(OnItemClick listener) {
        this.listener = listener;
    }

    public void submitList(List<Course> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Course c = data.get(position);
        h.tvName.setText(c.getName());
        h.tvTime.setText(dateFormat.format(c.getBeginTime()) + " - " + dateFormat.format(c.getEndTime()));

        Date startDate = c.getBeginTime();
        Date endDate = c.getEndTime();

        if (startDate != null && endDate != null) {
            int progress = mUtils.calculateProgressPercentage(startDate, endDate);

            h.progressBar.setProgress(progress);

        }


        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(c);
        });
    }

    public Course getCourseAt(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvTime;
        ProgressBar progressBar;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCourseName);
            tvTime = itemView.findViewById(R.id.tvTime);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
