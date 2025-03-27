package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {
    
    private List<ServiceResponse> servicesList;
    private List<ServiceResponse> filteredList;
    private OnServiceClickListener listener;
    
    public interface OnServiceClickListener {
        void onServiceClick(ServiceResponse service);
    }
    
    public ServicesAdapter(List<ServiceResponse> servicesList) {
        this.servicesList = servicesList;
        this.filteredList = new ArrayList<>(servicesList);
    }
    
    public ServicesAdapter(List<ServiceResponse> servicesList, OnServiceClickListener listener) {
        this.servicesList = servicesList;
        this.filteredList = new ArrayList<>(servicesList);
        this.listener = listener;
    }
    
    public void setFilteredList(List<ServiceResponse> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceResponse service = filteredList.get(position);
        holder.bind(service);
    }
    
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    
    class ServiceViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvServiceName;
        private final TextView tvSpecialty;
        private final TextView tvPrice;
        private final TextView tvOverview;
        
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onServiceClick(filteredList.get(position));
                }
            });
        }
        
        public void bind(ServiceResponse service) {
            tvServiceName.setText(service.getServiceName());
            
            if (service.getSpecialtyName() != null && !service.getSpecialtyName().isEmpty()) {
                tvSpecialty.setText(service.getSpecialtyName());
                tvSpecialty.setVisibility(View.VISIBLE);
            } else {
                tvSpecialty.setVisibility(View.GONE);
            }
            
            tvPrice.setText(String.format("$%.2f", service.getPrice()));
            
            if (service.getOverview() != null && !service.getOverview().isEmpty()) {
                tvOverview.setText(service.getOverview());
                tvOverview.setVisibility(View.VISIBLE);
            } else if (service.getDescription() != null && !service.getDescription().isEmpty()) {
                tvOverview.setText(service.getDescription());
                tvOverview.setVisibility(View.VISIBLE);
            } else {
                tvOverview.setVisibility(View.GONE);
            }
        }
    }
} 