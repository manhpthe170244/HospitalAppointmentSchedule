package com.example.project_prm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.DoctorSchedule;
import com.example.project_prm392.repository.DoctorRepository;
import com.example.project_prm392.utils.Resource;

import java.util.List;

public class DoctorViewModel extends ViewModel {
    private final DoctorRepository doctorRepository;
    
    // Constructor cho Dependency Injection
    public DoctorViewModel(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    public LiveData<Resource<List<Doctor>>> getAllDoctors() {
        return doctorRepository.getAllDoctors();
    }
    
    public LiveData<Resource<Doctor>> getDoctorById(int doctorId) {
        return doctorRepository.getDoctorById(doctorId);
    }
    
    public LiveData<Resource<List<Doctor>>> getDoctorsBySpecialty(int specialtyId) {
        return doctorRepository.getDoctorsBySpecialty(specialtyId);
    }
    
    public LiveData<Resource<List<Doctor>>> getDoctorsByService(int serviceId) {
        return doctorRepository.getDoctorsByService(serviceId);
    }
    
    public LiveData<Resource<List<DoctorSchedule>>> getDoctorSchedules(int doctorId) {
        return doctorRepository.getDoctorSchedules(doctorId);
    }
    
    public LiveData<Resource<List<Doctor>>> searchDoctors(String keyword) {
        return doctorRepository.searchDoctors(keyword);
    }
} 