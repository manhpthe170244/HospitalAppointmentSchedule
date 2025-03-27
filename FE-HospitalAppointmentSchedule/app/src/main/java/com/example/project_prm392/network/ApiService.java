package com.example.project_prm392.network;

import com.example.project_prm392.models.requests.LoginRequest;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.requests.ReservationUpdateRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;
import com.example.project_prm392.models.responses.LoginResponse;
import com.example.project_prm392.models.responses.PaginatedResponse;
import com.example.project_prm392.models.responses.ReservationDetailsResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.models.responses.StatisticsResponse;
import com.example.project_prm392.models.responses.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Auth endpoints
    @POST("api/auth/login")
    Call<BaseResponse<LoginResponse>> login(@Body LoginRequest loginRequest);
    
    @POST("api/auth/register")
    Call<BaseResponse<UserResponse>> register(@Body RegisterRequest registerRequest);
    
    @POST("api/auth/refresh")
    Call<BaseResponse<LoginResponse>> refreshToken(@Header("RefreshToken") String refreshToken);
    
    @POST("api/auth/revoke")
    Call<BaseResponse<Void>> revokeToken(@Header("RefreshToken") String refreshToken);
    
    // User endpoints
    @GET("api/users/{id}")
    Call<BaseResponse<UserResponse>> getUserById(@Path("id") int userId);
    
    @GET("api/users/current")
    Call<BaseResponse<UserResponse>> getCurrentUser();
    
    @PUT("api/users/{id}")
    Call<BaseResponse<UserResponse>> updateUser(@Path("id") int userId, @Body UserResponse userRequest);
    
    // Doctor endpoints
    @GET("api/doctors")
    Call<BaseResponse<List<DoctorResponse>>> getAllDoctors();
    
    @GET("api/doctors/{id}")
    Call<BaseResponse<DoctorDetailsResponse>> getDoctorById(@Path("id") int doctorId);
    
    @GET("api/doctors/specialty/{specialtyId}")
    Call<BaseResponse<List<DoctorResponse>>> getDoctorsBySpecialty(@Path("specialtyId") int specialtyId);
    
    @GET("api/doctors/service/{serviceId}")
    Call<BaseResponse<List<DoctorResponse>>> getDoctorsByService(@Path("serviceId") int serviceId);
    
    @GET("api/doctors/{doctorId}/schedules")
    Call<BaseResponse<List<DoctorScheduleResponse>>> getDoctorSchedules(@Path("doctorId") int doctorId);
    
    // Specialty endpoints
    @GET("api/specialties")
    Call<BaseResponse<List<SpecialtyResponse>>> getAllSpecialties();
    
    @GET("api/specialties/{id}")
    Call<BaseResponse<SpecialtyResponse>> getSpecialtyById(@Path("id") int specialtyId);
    
    // Service endpoints
    @GET("api/services")
    Call<BaseResponse<List<ServiceResponse>>> getAllServices();
    
    @GET("api/services/{id}")
    Call<BaseResponse<ServiceResponse>> getServiceById(@Path("id") int serviceId);
    
    @GET("api/services/specialty/{specialtyId}")
    Call<BaseResponse<List<ServiceResponse>>> getServicesBySpecialty(@Path("specialtyId") int specialtyId);
    
    // Reservation endpoints
    @GET("api/reservations/user")
    Call<BaseResponse<List<ReservationResponse>>> getUserReservations();
    
    @GET("api/reservations/{id}")
    Call<BaseResponse<ReservationResponse>> getReservationById(@Path("id") int reservationId);
    
    @GET("api/reservations/{id}/details")
    Call<BaseResponse<ReservationDetailsResponse>> getReservationDetails(@Path("id") int reservationId);
    
    @GET("api/reservations/patient/{patientId}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByPatient(@Path("patientId") int patientId);
    
    @POST("api/reservations")
    Call<BaseResponse<ReservationResponse>> createReservation(@Body ReservationCreateRequest request);
    
    @PUT("api/reservations/{id}")
    Call<BaseResponse<ReservationResponse>> updateReservation(
            @Path("id") int reservationId,
            @Body ReservationUpdateRequest reservationRequest
    );
    
    @POST("api/reservations/{id}/cancel")
    Call<BaseResponse<Boolean>> cancelReservation(@Path("id") int reservationId, @Query("reason") String reason);
    
    @PUT("api/reservations/{id}/approve")
    Call<BaseResponse<ReservationResponse>> approveReservation(@Path("id") int reservationId);
    
    @PUT("api/reservations/{id}/complete")
    Call<BaseResponse<ReservationResponse>> completeReservation(@Path("id") int reservationId);
    
    @POST("api/reservations/check-slot-availability")
    Call<BaseResponse<Boolean>> checkSlotAvailability(
            @Query("doctorScheduleId") int doctorScheduleId,
            @Query("appointmentDate") String appointmentDate
    );
    
    // Statistics endpoints
    @GET("api/statistics/dashboard")
    Call<BaseResponse<StatisticsResponse>> getDashboardStatistics();
} 