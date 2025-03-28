package com.example.project_prm392.ui.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.databinding.ActivityAdminDoctorFormBinding;
import com.example.project_prm392.models.requests.CreateDoctorRequest;
import com.example.project_prm392.models.requests.UpdateDoctorRequest;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.utils.ValidationUtils;
import com.example.project_prm392.viewmodels.DoctorViewModel;
import com.example.project_prm392.viewmodels.SpecialtyViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminDoctorFormActivity extends AppCompatActivity {
    private ActivityAdminDoctorFormBinding binding;
    private DoctorViewModel doctorViewModel;
    private SpecialtyViewModel specialtyViewModel;
    private int doctorId = -1;
    private Uri selectedImageUri;
    private final List<String> selectedSpecialtyIds = new ArrayList<>();

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(
        new ActivityResultContracts.GetContent(),
        uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.doctorImage);
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDoctorFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupViewModels();
        setupGenderDropdown();
        setupImagePicker();
        loadSpecialties();

        doctorId = getIntent().getIntExtra("doctorId", -1);
        if (doctorId != -1) {
            binding.toolbar.setTitle(R.string.edit_doctor);
            binding.passwordLayout.setVisibility(View.GONE);
            loadDoctorDetails();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupViewModels() {
        doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
        specialtyViewModel = new ViewModelProvider(this).get(SpecialtyViewModel.class);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            genders
        );
        binding.genderInput.setAdapter(adapter);
    }

    private void setupImagePicker() {
        binding.changeImageButton.setOnClickListener(v ->
            getContent.launch("image/*")
        );
    }

    private void loadSpecialties() {
        setLoading(true);
        specialtyViewModel.getAllSpecialties().observe(this, response -> {
            setLoading(false);
            if (response.isSuccess() && response.getData() != null) {
                setupSpecialtyChips(response.getData());
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpecialtyChips(List<SpecialtyResponse> specialties) {
        binding.specialtyChipGroup.removeAllViews();
        for (SpecialtyResponse specialty : specialties) {
            Chip chip = new Chip(this);
            chip.setText(specialty.getName());
            chip.setCheckable(true);
            chip.setChecked(selectedSpecialtyIds.contains(specialty.getId()));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSpecialtyIds.add(specialty.getId());
                } else {
                    selectedSpecialtyIds.remove(specialty.getId());
                }
            });
            binding.specialtyChipGroup.addView(chip);
        }
    }

    private void loadDoctorDetails() {
        setLoading(true);
        doctorViewModel.getDoctorById(doctorId).observe(this, response -> {
            setLoading(false);
            if (response.isSuccess() && response.getData() != null) {
                populateForm(response.getData());
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateForm(DoctorResponse doctor) {
        binding.nameInput.setText(doctor.getUserName());
        binding.emailInput.setText(doctor.getEmail());
        binding.phoneInput.setText(doctor.getPhone());
        binding.genderInput.setText(doctor.getGender(), false);
        binding.academicTitleInput.setText(doctor.getAcademicTitle());
        binding.degreeInput.setText(doctor.getDegree());
        binding.currentWorkInput.setText(doctor.getCurrentWork());
        binding.descriptionInput.setText(doctor.getDoctorDescription());
        binding.availabilitySwitch.setChecked(doctor.isAvailable());

        if (doctor.getAvatarUrl() != null && !doctor.getAvatarUrl().isEmpty()) {
            Glide.with(this)
                .load(doctor.getAvatarUrl())
                .circleCrop()
                .into(binding.doctorImage);
        }

        selectedSpecialtyIds.clear();
        selectedSpecialtyIds.addAll(doctor.getSpecialtyIds());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_doctor_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveDoctor();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDoctor() {
        if (!validateForm()) {
            return;
        }

        setLoading(true);
        if (doctorId == -1) {
            createDoctor();
        } else {
            updateDoctor();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        String name = binding.nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            binding.nameLayout.setError(getString(R.string.required_field));
            isValid = false;
        } else {
            binding.nameLayout.setError(null);
        }

        String email = binding.emailInput.getText().toString().trim();
        if (!ValidationUtils.isValidEmail(email)) {
            binding.emailLayout.setError(getString(R.string.invalid_email));
            isValid = false;
        } else {
            binding.emailLayout.setError(null);
        }

        if (doctorId == -1) {
            String password = binding.passwordInput.getText().toString();
            if (!ValidationUtils.isValidPassword(password)) {
                binding.passwordLayout.setError(getString(R.string.invalid_password));
                isValid = false;
            } else {
                binding.passwordLayout.setError(null);
            }
        }

        String phone = binding.phoneInput.getText().toString().trim();
        if (!ValidationUtils.isValidPhone(phone)) {
            binding.phoneLayout.setError(getString(R.string.invalid_phone));
            isValid = false;
        } else {
            binding.phoneLayout.setError(null);
        }

        String gender = binding.genderInput.getText().toString();
        if (gender.isEmpty()) {
            binding.genderLayout.setError(getString(R.string.required_field));
            isValid = false;
        } else {
            binding.genderLayout.setError(null);
        }

        if (selectedSpecialtyIds.isEmpty()) {
            Toast.makeText(this, R.string.select_at_least_one_specialty, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void createDoctor() {
        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setUserName(binding.nameInput.getText().toString().trim());
        request.setEmail(binding.emailInput.getText().toString().trim());
        request.setPassword(binding.passwordInput.getText().toString());
        request.setPhone(binding.phoneInput.getText().toString().trim());
        request.setGender(binding.genderInput.getText().toString());
        request.setAcademicTitle(binding.academicTitleInput.getText().toString().trim());
        request.setDegree(binding.degreeInput.getText().toString().trim());
        request.setCurrentWork(binding.currentWorkInput.getText().toString().trim());
        request.setDoctorDescription(binding.descriptionInput.getText().toString().trim());
        request.setSpecialtyIds(selectedSpecialtyIds);

        if (selectedImageUri != null) {
            // TODO: Upload image and set avatarUrl
        }

        doctorViewModel.createDoctor(request).observe(this, response -> {
            setLoading(false);
            if (response.isSuccess()) {
                Toast.makeText(this, R.string.doctor_created, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDoctor() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setDoctorId(doctorId);
        request.setUserName(binding.nameInput.getText().toString().trim());
        request.setPhone(binding.phoneInput.getText().toString().trim());
        request.setGender(binding.genderInput.getText().toString());
        request.setAcademicTitle(binding.academicTitleInput.getText().toString().trim());
        request.setDegree(binding.degreeInput.getText().toString().trim());
        request.setCurrentWork(binding.currentWorkInput.getText().toString().trim());
        request.setDoctorDescription(binding.descriptionInput.getText().toString().trim());
        request.setSpecialtyIds(selectedSpecialtyIds);
        request.setAvailable(binding.availabilitySwitch.isChecked());

        if (selectedImageUri != null) {
            // TODO: Upload image and set avatarUrl
        }

        doctorViewModel.updateDoctor(request).observe(this, response -> {
            setLoading(false);
            if (response.isSuccess()) {
                Toast.makeText(this, R.string.doctor_updated, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.nameInput.setEnabled(!isLoading);
        binding.emailInput.setEnabled(!isLoading);
        binding.passwordInput.setEnabled(!isLoading);
        binding.phoneInput.setEnabled(!isLoading);
        binding.genderInput.setEnabled(!isLoading);
        binding.academicTitleInput.setEnabled(!isLoading);
        binding.degreeInput.setEnabled(!isLoading);
        binding.currentWorkInput.setEnabled(!isLoading);
        binding.descriptionInput.setEnabled(!isLoading);
        binding.changeImageButton.setEnabled(!isLoading);
        binding.availabilitySwitch.setEnabled(!isLoading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 