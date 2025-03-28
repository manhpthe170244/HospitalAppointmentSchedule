package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.RevenueResponse;

import java.util.List;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder> {
    private List<RevenueResponse> revenues;

    public RevenueAdapter(List<RevenueResponse> revenues) {
        this.revenues = revenues;
    }

    @NonNull
    @Override
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenue, parent, false);
        return new RevenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        RevenueResponse revenue = revenues.get(position);
        holder.dateText.setText(revenue.getDate());
        holder.amountText.setText(String.format("$%.2f", revenue.getAmount()));
        holder.sourceText.setText(revenue.getSource());
    }

    @Override
    public int getItemCount() {
        return revenues.size();
    }

    class RevenueViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView amountText;
        TextView sourceText;

        RevenueViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            amountText = itemView.findViewById(R.id.amountText);
            sourceText = itemView.findViewById(R.id.sourceText);
        }
    }
} 