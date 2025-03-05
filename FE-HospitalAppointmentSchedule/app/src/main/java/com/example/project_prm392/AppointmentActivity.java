package com.example.project_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentActivity extends AppCompatActivity {

    private TextView textViewDoctor;
    private DatePicker datePicker;
    private Spinner spinnerRooms;
    private Button buttonBook;
    private String[] availableRooms = {"Phòng 101", "Phòng 102", "Phòng 103"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        textViewDoctor = findViewById(R.id.textViewDoctor);
        datePicker = findViewById(R.id.datePicker);
        spinnerRooms = findViewById(R.id.spinnerRooms);
        buttonBook = findViewById(R.id.buttonBook);

        String doctor = getIntent().getStringExtra("doctor");
        String department = getIntent().getStringExtra("department");
        textViewDoctor.setText("Đặt lịch với: " + doctor);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableRooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRooms.setAdapter(adapter);

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String room = spinnerRooms.getSelectedItem().toString();

                String appointmentDetails = "Bác sĩ: " + doctor + "\nKhoa: " + department + "\nNgày: " + day + "/" + month + "/" + year + "\nPhòng: " + room;

                Intent intent = new Intent(AppointmentActivity.this, ConfirmationActivity.class);
                intent.putExtra("appointmentDetails", appointmentDetails);
                startActivity(intent);
            }
        });
    }
}
