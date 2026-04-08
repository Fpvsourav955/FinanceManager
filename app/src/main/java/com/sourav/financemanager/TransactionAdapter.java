package com.sourav.financemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> list = new ArrayList<>();

    public void setData(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Transaction t = list.get(position);

        holder.tvCategory.setText(
                (t.note != null && !t.note.isEmpty()) ? t.note : t.category
        );


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        String date = sdf.format(new Date(t.date));

        holder.tvNote.setText(t.category + " • " + date);

        if (t.type.equals("income")) {
            holder.tvAmount.setText("+₹" + t.amount);
            holder.tvAmount.setTextColor(0xFF2E7D32);
        } else {
            holder.tvAmount.setText("-₹" + t.amount);
            holder.tvAmount.setTextColor(0xFFE53935);
        }

        switch (t.category.toLowerCase()) {

            case "shopping":
                holder.imgCategory.setImageResource(R.drawable.ic_shopping);
                break;

            case "dining":
                holder.imgCategory.setImageResource(R.drawable.ic_dining);
                break;

            case "transport":
                holder.imgCategory.setImageResource(R.drawable.ic_transport);
                break;

            case "movies":
            case "movie":
                holder.imgCategory.setImageResource(R.drawable.ic_movie);
                break;

            case "rent":
                holder.imgCategory.setImageResource(R.drawable.ic_rent);
                break;

            case "health":
            case "medical":
                holder.imgCategory.setImageResource(R.drawable.ic_medical);
                break;

            case "gym":
                holder.imgCategory.setImageResource(R.drawable.ic_gym);
                break;

            default:
                holder.imgCategory.setImageResource(R.drawable.ic_others);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvCategory, tvNote, tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}