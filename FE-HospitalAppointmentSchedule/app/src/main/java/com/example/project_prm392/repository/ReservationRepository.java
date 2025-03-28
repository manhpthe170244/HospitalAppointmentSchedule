package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiService;
import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.requests.ReservationUpdateRequest;
import com.example.project_prm392.models.requests.PaymentCreateRequest;
import com.example.project_prm392.models.requests.MedicalRecordCreateRequest;
import com.example.project_prm392.models.requests.FeedbackCreateRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ReservationDetailsResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.models.responses.PaymentResponse;
import com.example.project_prm392.models.responses.MedicalRecordResponse;
import com.example.project_prm392.models.responses.FeedbackResponse;
import com.example.project_prm392.models.responses.PaginatedResponse;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class ReservationRepository {
    private final ApiService apiService;

    @Inject
    public ReservationRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<BaseResponse<PaginatedResponse<ReservationResponse>>> getUserReservations() {
        MutableLiveData<BaseResponse<PaginatedResponse<ReservationResponse>>> result = new MutableLiveData<>();
        
        apiService.getUserReservations().enqueue(new Callback<BaseResponse<PaginatedResponse<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<PaginatedResponse<ReservationResponse>>> call, 
                                 Response<BaseResponse<PaginatedResponse<ReservationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservations", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PaginatedResponse<ReservationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> getReservationById(int reservationId) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.getReservationById(reservationId).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationDetailsResponse>> getReservationDetails(int reservationId) {
        MutableLiveData<BaseResponse<ReservationDetailsResponse>> result = new MutableLiveData<>();
        
        apiService.getReservationDetails(reservationId).enqueue(new Callback<BaseResponse<ReservationDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationDetailsResponse>> call, 
                                 Response<BaseResponse<ReservationDetailsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservation details", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationDetailsResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<ReservationResponse>>> getReservationsByPatient(int patientId) {
        MutableLiveData<BaseResponse<List<ReservationResponse>>> result = new MutableLiveData<>();
        
        apiService.getReservationsByPatient(patientId).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, 
                                 Response<BaseResponse<List<ReservationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get patient reservations", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<ReservationResponse>>> getReservationsByDateRange(Date startDate, Date endDate) {
        MutableLiveData<BaseResponse<List<ReservationResponse>>> result = new MutableLiveData<>();
        
        apiService.getReservationsByDateRange(startDate, endDate).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, 
                                 Response<BaseResponse<List<ReservationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservations by date range", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<ReservationResponse>>> getReservationsByStatus(String status) {
        MutableLiveData<BaseResponse<List<ReservationResponse>>> result = new MutableLiveData<>();
        
        apiService.getReservationsByStatus(status).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, 
                                 Response<BaseResponse<List<ReservationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservations by status", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<ReservationResponse>>> getReservationsBySpecialty(int specialtyId, Date date) {
        MutableLiveData<BaseResponse<List<ReservationResponse>>> result = new MutableLiveData<>();
        
        apiService.getReservationsBySpecialty(specialtyId, date).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, 
                                 Response<BaseResponse<List<ReservationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get reservations by specialty", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> createReservation(ReservationCreateRequest request) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.createReservation(request).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to create reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> updateReservation(int reservationId, ReservationUpdateRequest request) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.updateReservation(reservationId, request).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to update reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> cancelReservation(int reservationId, String reason) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.cancelReservation(reservationId, reason).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to cancel reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> approveReservation(int reservationId) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.approveReservation(reservationId).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to approve reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<ReservationResponse>> completeReservation(int reservationId) {
        MutableLiveData<BaseResponse<ReservationResponse>> result = new MutableLiveData<>();
        
        apiService.completeReservation(reservationId).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, 
                                 Response<BaseResponse<ReservationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to complete reservation", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<Boolean>> checkSlotAvailability(int doctorScheduleId, Date appointmentDate) {
        MutableLiveData<BaseResponse<Boolean>> result = new MutableLiveData<>();
        
        apiService.checkSlotAvailability(doctorScheduleId, appointmentDate).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to check slot availability", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<PaymentResponse>> createPayment(PaymentCreateRequest request) {
        MutableLiveData<BaseResponse<PaymentResponse>> result = new MutableLiveData<>();
        
        apiService.createPayment(request).enqueue(new Callback<BaseResponse<PaymentResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<PaymentResponse>> call, 
                                 Response<BaseResponse<PaymentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to create payment", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PaymentResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<PaymentResponse>>> getPaymentsByReservation(int reservationId) {
        MutableLiveData<BaseResponse<List<PaymentResponse>>> result = new MutableLiveData<>();
        
        apiService.getPaymentsByReservation(reservationId).enqueue(new Callback<BaseResponse<List<PaymentResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PaymentResponse>>> call, 
                                 Response<BaseResponse<List<PaymentResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get payments", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PaymentResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<MedicalRecordResponse>> createMedicalRecord(MedicalRecordCreateRequest request) {
        MutableLiveData<BaseResponse<MedicalRecordResponse>> result = new MutableLiveData<>();
        
        apiService.createMedicalRecord(request).enqueue(new Callback<BaseResponse<MedicalRecordResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<MedicalRecordResponse>> call, 
                                 Response<BaseResponse<MedicalRecordResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to create medical record", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<MedicalRecordResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<MedicalRecordResponse>>> getMedicalRecordsByPatient(int patientId) {
        MutableLiveData<BaseResponse<List<MedicalRecordResponse>>> result = new MutableLiveData<>();
        
        apiService.getMedicalRecordsByPatient(patientId).enqueue(new Callback<BaseResponse<List<MedicalRecordResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MedicalRecordResponse>>> call, 
                                 Response<BaseResponse<List<MedicalRecordResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get medical records", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MedicalRecordResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<FeedbackResponse>> createFeedback(FeedbackCreateRequest request) {
        MutableLiveData<BaseResponse<FeedbackResponse>> result = new MutableLiveData<>();
        
        apiService.createFeedback(request).enqueue(new Callback<BaseResponse<FeedbackResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<FeedbackResponse>> call, 
                                 Response<BaseResponse<FeedbackResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to create feedback", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<FeedbackResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }
} 