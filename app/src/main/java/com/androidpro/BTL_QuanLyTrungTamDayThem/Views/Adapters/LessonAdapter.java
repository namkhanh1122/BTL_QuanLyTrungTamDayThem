package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.VH> {

    public interface OnItemClick {
        void onClick(Lesson lesson);
    }

    private final List<Lesson> data = new ArrayList<>();
    private final LessonAdapter.OnItemClick listener;

    public LessonAdapter(LessonAdapter.OnItemClick listener) {
        this.listener = listener;
    }

    public void submitList(List<Lesson> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    public Lesson getLessonAt(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public LessonAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson_chip, parent, false);
        return new LessonAdapter.VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.VH h, int position) {
        Lesson l = data.get(position);
        h.tvLessonTitle.setText(l.getTitle());

        Date beginTime = l.getBeginTime();

        if (beginTime != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
            h.tvLessonTime.setText(timeFormat.format(beginTime));

            Locale vietnameseLocale = new Locale("vi", "VN");
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", vietnameseLocale);

            String dayOfWeek = dayFormat.format(beginTime);

            h.tvLessonDay.setText(dayOfWeek);

        } else {
            // Xử lý nếu data bị null
            h.tvLessonTime.setText("N/A");
            h.tvLessonDay.setText("N/A");
        }


        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(l);
        });

        // bind status badge
        try {
            if (h.tvStatusBadge != null) {
                String label = "Sắp";
                int bg = Color.parseColor("#BDBDBD");
                int textColor = Color.BLACK;
                if (l.getStatus() != null) {
                    switch (l.getStatus()) {
                        case Active:
                            label = "Đang";
                            bg = Color.parseColor("#4CAF50");
                            textColor = Color.WHITE;
                            break;
                        case Completed:
                            label = "Hoàn thành";
                            bg = Color.parseColor("#2196F3");
                            textColor = Color.WHITE;
                            break;
                        case Canceled:
                            label = "Đã hủy";
                            bg = Color.parseColor("#E57373");
                            textColor = Color.WHITE;
                            break;
                        default:
                            label = "Sắp";
                            bg = Color.parseColor("#BDBDBD");
                            textColor = Color.BLACK;
                    }
                }
                h.tvStatusBadge.setText(label);
                h.tvStatusBadge.setChipBackgroundColor(ColorStateList.valueOf(bg));
                h.tvStatusBadge.setTextColor(textColor);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvLessonTitle, tvLessonTime, tvLessonDay;
        Chip tvStatusBadge;
        VH(@NonNull View itemView) {
            super(itemView);
            tvLessonTitle = itemView.findViewById(R.id.tvLessonTitle);
            tvLessonTime = itemView.findViewById(R.id.tvLessonTime);
            tvLessonDay = itemView.findViewById(R.id.tvLessonDay);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
        }
    }
}