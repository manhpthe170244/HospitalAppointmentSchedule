package com.example.project_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorListActivity extends AppCompatActivity {

    private ListView listViewDoctors;
    private TextView textViewDepartment;
    private String[] doctors = {"BS. Nguyễn Văn A - Khoa Nội", "BS. Trần Thị B - Khoa Ngoại", "BS. Lê Văn C - Khoa Nhi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        textViewDepartment = findViewById(R.id.textViewDepartment);
        listViewDoctors = findViewById(R.id.listViewDoctors);

        String department = getIntent().getStringExtra("department");
        textViewDepartment.setText("Danh sách bác sĩ - " + department);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctors);
        listViewDoctors.setAdapter(adapter);

        listViewDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDoctor = doctors[position];
                Intent intent = new Intent(DoctorListActivity.this, AppointmentActivity.class);
                intent.putExtra("doctor", selectedDoctor);
                intent.putExtra("department", department);
                startActivity(intent);
            }
        });
    }
}
