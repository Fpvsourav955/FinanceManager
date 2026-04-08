package com.sourav.financemanager;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryTotal> data = new ArrayList<>();
    private double grandTotal = 0;
    public void setData(List<CategoryTotal> list) {
        this.data = list;
        grandTotal = 0;
        for (CategoryTotal item : list) {
            grandTotal += item.total;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return data.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        CategoryTotal item = data.get(position);

        h.tvCategory.setText(item.category);

        int percent = grandTotal > 0
                ? (int) Math.round((item.total / grandTotal) * 100)
                : 0;
        h.tvPercent.setText(percent + "%");

        int[] colors = {0xFF075292, 0xFF2B5DA8, 0xFF81AEFE, 0xFF306BAC, 0xFFABC7FF};
        int color = colors[position % colors.length];
        h.colorDot.getBackground().setTint(color);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvPercent;
        View colorDot;

        ViewHolder(View v) {
            super(v);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvPercent  = v.findViewById(R.id.tvPercent);
            colorDot   = v.findViewById(R.id.colorDot);
        }
    }
}