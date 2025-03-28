package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.NotificationResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationResponse> notifications = new ArrayList<>();
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationResponse notification);
        void onNotificationDelete(NotificationResponse notification);
    }

    public NotificationAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationResponse notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<NotificationResponse> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;
        private TextView titleTextView;
        private TextView messageTextView;
        private TextView timeTextView;
        private ImageView deleteButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNotificationClick(notifications.get(position));
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNotificationDelete(notifications.get(position));
                }
            });
        }

        public void bind(NotificationResponse notification) {
            titleTextView.setText(notification.getTitle());
            messageTextView.setText(notification.getMessage());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            timeTextView.setText(sdf.format(notification.getCreatedAt()));

            // Set icon based on notification type
            switch (notification.getType()) {
                case "APPOINTMENT":
                    iconImageView.setImageResource(R.drawable.ic_appointment);
                    break;
                case "PAYMENT":
                    iconImageView.setImageResource(R.drawable.ic_payment);
                    break;
                case "MEDICAL_RECORD":
                    iconImageView.setImageResource(R.drawable.ic_medical_record);
                    break;
                default:
                    iconImageView.setImageResource(R.drawable.ic_notification);
            }

            // Set background color based on read status
            if (notification.isRead()) {
                itemView.setBackgroundResource(R.drawable.bg_notification_read);
            } else {
                itemView.setBackgroundResource(R.drawable.bg_notification_unread);
            }
        }
    }
} 