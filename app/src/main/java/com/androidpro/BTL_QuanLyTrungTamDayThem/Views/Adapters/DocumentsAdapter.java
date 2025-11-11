package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Document;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.VH> {
    private final List<Document> data = new ArrayList<>();

    public void submitList(List<Document> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Document d = data.get(position);
        holder.tvTitle.setText(d.getTitle() != null ? d.getTitle() : "(Không tên)");
        holder.tvDesc.setText(d.getDescription() != null ? d.getDescription() : "");

        holder.itemView.setOnClickListener(v -> {
            String url = d.getUrl();
            if (url != null && !url.isEmpty()) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    public Document getDocumentAt(int position) {
        if (position < 0 || position >= data.size()) return null;
        return data.get(position);
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvDocTitle);
            tvDesc = itemView.findViewById(R.id.tvDocDesc);
        }
    }
}
