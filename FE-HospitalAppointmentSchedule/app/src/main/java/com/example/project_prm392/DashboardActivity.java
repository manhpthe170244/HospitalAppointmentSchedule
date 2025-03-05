package com.example.project_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ListView listViewDepartments;
    private String[] departments = {"Khoa Nội", "Khoa Ngoại", "Khoa Nhi", "Khoa Sản"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        listViewDepartments = findViewById(R.id.listViewDepartments);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, departments);
        listViewDepartments.setAdapter(adapter);

        listViewDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDepartment = departments[position];
                Intent intent = new Intent(DashboardActivity.this, DoctorListActivity.class);
                intent.putExtra("department", selectedDepartment);
                startActivity(intent);
            }
        });
    }
}
