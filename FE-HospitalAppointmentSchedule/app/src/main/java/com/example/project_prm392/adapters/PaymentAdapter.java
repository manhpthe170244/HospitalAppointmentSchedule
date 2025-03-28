package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.PaymentResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<PaymentResponse> payments = new ArrayList<>();

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentResponse payment = payments.get(position);
        holder.bind(payment);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void setPayments(List<PaymentResponse> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView amountTextView;
        private TextView statusTextView;
        private TextView methodTextView;
        private TextView dateTextView;
        private TextView transactionIdTextView;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            methodTextView = itemView.findViewById(R.id.methodTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            transactionIdTextView = itemView.findViewById(R.id.transactionIdTextView);
        }

        public void bind(PaymentResponse payment) {
            amountTextView.setText(String.format("%,.0f VNĐ", payment.getAmount()));
            statusTextView.setText(payment.getStatus());
            methodTextView.setText(payment.getPaymentMethod());
            transactionIdTextView.setText(payment.getTransactionId());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            dateTextView.setText(sdf.format(payment.getCreatedAt()));

            // Set status color
            switch (payment.getStatus().toUpperCase()) {
                case "SUCCESS":
                    statusTextView.setTextColor(itemView.getContext().getColor(R.color.success));
                    break;
                case "PENDING":
                    statusTextView.setTextColor(itemView.getContext().getColor(R.color.warning));
                    break;
                case "FAILED":
                    statusTextView.setTextColor(itemView.getContext().getColor(R.color.error));
                    break;
                default:
                    statusTextView.setTextColor(itemView.getContext().getColor(R.color.text_primary));
            }
        }
    }
} 