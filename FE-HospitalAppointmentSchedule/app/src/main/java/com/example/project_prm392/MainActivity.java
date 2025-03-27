package com.example.project_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project_prm392.repository.AuthRepository;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.ui.user.BookAppointmentActivity;
import com.example.project_prm392.ui.user.MyAppointmentsActivity;
import com.example.project_prm392.ui.user.ViewDoctorsActivity;
import com.example.project_prm392.ui.user.ViewServicesActivity;
import com.example.project_prm392.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    
    @Inject
    AuthRepository authRepository;
    
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Set up navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        
        // Set up navigation header with user info
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        
        // Set user info
        String userName = sessionManager.getUserName();
        String userEmail = sessionManager.getUserEmail();
        if (userName != null) {
            tvUserName.setText(userName);
        }
        if (userEmail != null) {
            tvUserEmail.setText(userEmail);
        }
        
        // Set up drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        // Set up navigation view listener
        navigationView.setNavigationItemSelectedListener(this);
        
        // Show/hide menu items based on user role
        String userRole = sessionManager.getUserRole();
        if (userRole != null) {
            if (!userRole.equalsIgnoreCase("Admin") && !userRole.equalsIgnoreCase("Doctor")) {
                navigationView.getMenu().findItem(R.id.nav_admin_dashboard).setVisible(false);
            }
        }
        
        // Check if user is logged in
        if (!authRepository.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_doctors) {
            startActivity(new Intent(this, ViewDoctorsActivity.class));
        } else if (id == R.id.nav_services) {
            startActivity(new Intent(this, ViewServicesActivity.class));
        } else if (id == R.id.nav_book_appointment) {
            startActivity(new Intent(this, BookAppointmentActivity.class));
        } else if (id == R.id.nav_my_appointments) {
            startActivity(new Intent(this, MyAppointmentsActivity.class));
        } else if (id == R.id.nav_logout) {
            authRepository.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
} 