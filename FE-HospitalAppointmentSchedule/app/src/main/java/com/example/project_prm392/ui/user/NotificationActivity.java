package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.NotificationAdapter;
import com.example.project_prm392.models.responses.NotificationResponse;
import com.example.project_prm392.repository.NotificationRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private FloatingActionButton markAllReadButton;

    @Inject
    NotificationRepository notificationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông báo");
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        markAllReadButton = findViewById(R.id.markAllReadButton);

        // Setup RecyclerView
        adapter = new NotificationAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup mark all read button
        markAllReadButton.setOnClickListener(v -> markAllNotificationsAsRead());

        // Load notifications
        loadNotifications();
    }

    private void loadNotifications() {
        notificationRepository.getUserNotifications().observe(this, response -> {
            if (response.isSuccess() && response.getData() != null) {
                adapter.setNotifications(response.getData());
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAllNotificationsAsRead() {
        notificationRepository.markAllNotificationsAsRead().observe(this, response -> {
            if (response.isSuccess()) {
                Toast.makeText(this, "Đã đánh dấu tất cả thông báo đã đọc", Toast.LENGTH_SHORT).show();
                loadNotifications(); // Reload notifications
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNotificationClick(NotificationResponse notification) {
        if (!notification.isRead()) {
            markNotificationAsRead(notification.getId());
        }
        // Handle notification click based on type
        switch (notification.getType()) {
            case "APPOINTMENT":
                // Navigate to appointment details
                break;
            case "PAYMENT":
                // Navigate to payment details
                break;
            case "MEDICAL_RECORD":
                // Navigate to medical record details
                break;
        }
    }

    @Override
    public void onNotificationDelete(NotificationResponse notification) {
        notificationRepository.deleteNotification(notification.getId()).observe(this, response -> {
            if (response.isSuccess()) {
                Toast.makeText(this, "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                loadNotifications(); // Reload notifications
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markNotificationAsRead(int notificationId) {
        notificationRepository.markNotificationAsRead(notificationId).observe(this, response -> {
            if (response.isSuccess()) {
                loadNotifications(); // Reload notifications
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 