package com.example.project_prm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.model.Service;
import com.example.project_prm392.repository.ServiceRepository;
import com.example.project_prm392.utils.Resource;

import java.util.List;

public class ServiceViewModel extends ViewModel {
    private final ServiceRepository serviceRepository;
    
    // Constructor cho Dependency Injection
    public ServiceViewModel(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    
    public LiveData<Resource<List<Service>>> getAllServices() {
        return serviceRepository.getAllServices();
    }
    
    public LiveData<Resource<Service>> getServiceById(int serviceId) {
        return serviceRepository.getServiceById(serviceId);
    }
    
    public LiveData<Resource<List<Service>>> getServicesBySpecialty(int specialtyId) {
        return serviceRepository.getServicesBySpecialty(specialtyId);
    }
    
    public LiveData<Resource<List<Service>>> getServicesByDoctor(int doctorId) {
        return serviceRepository.getServicesByDoctor(doctorId);
    }
    
    public LiveData<Resource<List<Service>>> searchServices(String keyword) {
        return serviceRepository.searchServices(keyword);
    }
} 