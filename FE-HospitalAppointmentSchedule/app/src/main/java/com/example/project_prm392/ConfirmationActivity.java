package com.example.project_prm392;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView textViewConfirmation = findViewById(R.id.textViewConfirmation);
        String appointmentDetails = getIntent().getStringExtra("appointmentDetails");
        textViewConfirmation.setText("Lịch hẹn của bạn đã được đặt thành công!\n\n" + appointmentDetails);
    }
}