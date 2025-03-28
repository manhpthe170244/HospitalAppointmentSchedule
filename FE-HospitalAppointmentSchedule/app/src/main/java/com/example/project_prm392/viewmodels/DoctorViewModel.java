package com.example.project_prm392.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.models.requests.CreateDoctorRequest;
import com.example.project_prm392.models.requests.UpdateDoctorRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.repository.DoctorRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DoctorViewModel extends ViewModel {
    private final DoctorRepository doctorRepository;
    private final MutableLiveData<DoctorResponse> selectedDoctor = new MutableLiveData<>();

    @Inject
    public DoctorViewModel(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public LiveData<BaseResponse<List<DoctorResponse>>> getAllDoctors() {
        return doctorRepository.getAllDoctors();
    }

    public LiveData<BaseResponse<DoctorResponse>> getDoctorById(int doctorId) {
        return doctorRepository.getDoctorById(doctorId);
    }

    public LiveData<BaseResponse<DoctorResponse>> createDoctor(CreateDoctorRequest request) {
        return doctorRepository.createDoctor(request);
    }

    public LiveData<BaseResponse<DoctorResponse>> updateDoctor(UpdateDoctorRequest request) {
        return doctorRepository.updateDoctor(request);
    }

    public LiveData<BaseResponse<Void>> deleteDoctor(int doctorId) {
        return doctorRepository.deleteDoctor(doctorId);
    }

    public void setSelectedDoctor(DoctorResponse doctor) {
        selectedDoctor.setValue(doctor);
    }

    public LiveData<DoctorResponse> getSelectedDoctor() {
        return selectedDoctor;
    }

    public void clearSelectedDoctor() {
        selectedDoctor.setValue(null);
    }
} 