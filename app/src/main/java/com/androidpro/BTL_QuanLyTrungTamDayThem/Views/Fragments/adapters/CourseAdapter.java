package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> {

    public interface OnItemClick {
        void onClick(Course course);
    }

    private final List<Course> data = new ArrayList<>();
    private final OnItemClick listener;

    public CourseAdapter(OnItemClick listener) {
        this.listener = listener;
    }

    public void submitList(List<Course> list){
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
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(c);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCourseName);
        }
    }
}
