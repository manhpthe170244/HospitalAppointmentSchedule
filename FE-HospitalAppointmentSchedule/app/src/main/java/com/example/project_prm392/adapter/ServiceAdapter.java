package com.example.project_prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.model.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private final Context context;
    private final OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
        void onViewDetailsClick(Service service);
    }

    public ServiceAdapter(Context context, OnServiceClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.serviceList = new ArrayList<>();
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        
        // Thiết lập dữ liệu
        holder.tvServiceName.setText(service.getServiceName());
        
        // Format giá tiền
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvServicePrice.setText(numberFormat.format(service.getPrice()));
        
        // Hiển thị thời gian
        holder.tvServiceDuration.setText("Thời gian: " + service.getDuration());
        
        // Load ảnh dịch vụ nếu có
        if (service.getImage() != null && !service.getImage().isEmpty()) {
            Glide.with(context)
                    .load(service.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.ivServiceImage);
        }
        
        // Thiết lập click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onServiceClick(service);
            }
        });
        
        holder.tvViewDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailsClick(service);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView ivServiceImage;
        TextView tvServiceName, tvServicePrice, tvServiceDuration, tvViewDetails;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivServiceImage = itemView.findViewById(R.id.ivServiceImage);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvServiceDuration = itemView.findViewById(R.id.tvServiceDuration);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
        }
    }
} 