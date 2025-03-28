package com.example.project_prm392.network;

import com.example.project_prm392.models.requests.LoginRequest;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.requests.ReservationUpdateRequest;
import com.example.project_prm392.models.requests.PaymentCreateRequest;
import com.example.project_prm392.models.requests.MedicalRecordCreateRequest;
import com.example.project_prm392.models.requests.FeedbackCreateRequest;
import com.example.project_prm392.models.requests.DoctorRequest;
import com.example.project_prm392.models.requests.SpecialtyRequest;
import com.example.project_prm392.models.requests.ServiceRequest;
import com.example.project_prm392.models.requests.RatingRequest;
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
import com.example.project_prm392.models.responses.PaymentResponse;
import com.example.project_prm392.models.responses.MedicalRecordResponse;
import com.example.project_prm392.models.responses.FeedbackResponse;
import com.example.project_prm392.models.responses.RatingResponse;
import com.example.project_prm392.models.responses.NotificationResponse;

import java.util.List;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    
    @POST("api/auth/refresh-token")
    Call<BaseResponse<LoginResponse>> refreshToken(@Body String refreshToken);
    
    @POST("api/auth/revoke-token")
    Call<BaseResponse<Void>> revokeToken(@Body String refreshToken);
    
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
    @GET("api/reservations")
    Call<BaseResponse<PaginatedResponse<ReservationResponse>>> getReservations(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("searchTerm") String searchTerm
    );
    
    @GET("api/reservations/{id}")
    Call<BaseResponse<ReservationResponse>> getReservationById(@Path("id") int reservationId);
    
    @GET("api/reservations/{id}/details")
    Call<BaseResponse<ReservationDetailsResponse>> getReservationDetails(@Path("id") int reservationId);
    
    @GET("api/reservations/patient/{patientId}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByPatient(@Path("patientId") int patientId);
    
    @GET("api/reservations/date-range")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByDateRange(
            @Query("startDate") Date startDate,
            @Query("endDate") Date endDate
    );
    
    @GET("api/reservations/status/{status}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByStatus(@Path("status") String status);
    
    @GET("api/reservations/specialty/{specialtyId}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsBySpecialty(
            @Path("specialtyId") int specialtyId,
            @Query("date") Date date
    );
    
    @POST("api/reservations")
    Call<BaseResponse<ReservationResponse>> createReservation(@Body ReservationCreateRequest request);
    
    @PUT("api/reservations/{id}")
    Call<BaseResponse<ReservationResponse>> updateReservation(
            @Path("id") int reservationId,
            @Body ReservationUpdateRequest request
    );
    
    @PUT("api/reservations/{id}/cancel")
    Call<BaseResponse<ReservationResponse>> cancelReservation(
            @Path("id") int reservationId,
            @Body String reason
    );
    
    @PUT("api/reservations/{id}/approve")
    Call<BaseResponse<ReservationResponse>> approveReservation(@Path("id") int reservationId);
    
    @PUT("api/reservations/{id}/complete")
    Call<BaseResponse<ReservationResponse>> completeReservation(@Path("id") int reservationId);
    
    @POST("api/reservations/check-slot-availability")
    Call<BaseResponse<Boolean>> checkSlotAvailability(
            @Query("doctorScheduleId") int doctorScheduleId,
            @Query("appointmentDate") Date appointmentDate
    );
    
    // Payment endpoints
    @POST("api/reservations/payments")
    Call<BaseResponse<PaymentResponse>> createPayment(@Body PaymentCreateRequest request);
    
    @GET("api/reservations/{id}/payments")
    Call<BaseResponse<List<PaymentResponse>>> getPaymentsByReservation(@Path("id") int reservationId);
    
    // Medical Records endpoints
    @POST("api/reservations/medical-records")
    Call<BaseResponse<MedicalRecordResponse>> createMedicalRecord(@Body MedicalRecordCreateRequest request);
    
    @GET("api/reservations/patient/{patientId}/medical-records")
    Call<BaseResponse<List<MedicalRecordResponse>>> getMedicalRecordsByPatient(@Path("patientId") int patientId);
    
    // Feedback endpoints
    @POST("api/reservations/feedbacks")
    Call<BaseResponse<FeedbackResponse>> createFeedback(@Body FeedbackCreateRequest request);
    
    // Statistics endpoints
    @GET("api/statistics/dashboard")
    Call<BaseResponse<StatisticsResponse>> getDashboardStatistics();
    
    @GET("api/statistics/revenue-by-specialty")
    Call<BaseResponse<List<StatisticsResponse>>> getRevenueBySpecialty(
            @Query("startDate") Date startDate,
            @Query("endDate") Date endDate
    );
    
    @GET("api/statistics/doctor-performance")
    Call<BaseResponse<List<StatisticsResponse>>> getDoctorPerformance(
            @Query("startDate") Date startDate,
            @Query("endDate") Date endDate
    );

    // Admin Doctor Management
    @POST("api/doctors")
    Call<BaseResponse<DoctorResponse>> createDoctor(@Body DoctorRequest request);
    
    @PUT("api/doctors/{id}")
    Call<BaseResponse<DoctorResponse>> updateDoctor(@Path("id") int doctorId, @Body DoctorRequest request);
    
    @DELETE("api/doctors/{id}")
    Call<BaseResponse<Void>> deleteDoctor(@Path("id") int doctorId);
    
    @POST("api/doctors/{id}/schedules")
    Call<BaseResponse<DoctorScheduleResponse>> createDoctorSchedule(@Path("id") int doctorId, @Body DoctorScheduleRequest request);
    
    @PUT("api/doctors/{id}/schedules/{scheduleId}")
    Call<BaseResponse<DoctorScheduleResponse>> updateDoctorSchedule(
            @Path("id") int doctorId,
            @Path("scheduleId") int scheduleId,
            @Body DoctorScheduleRequest request
    );
    
    @DELETE("api/doctors/{id}/schedules/{scheduleId}")
    Call<BaseResponse<Void>> deleteDoctorSchedule(@Path("id") int doctorId, @Path("scheduleId") int scheduleId);

    // Admin Specialty Management
    @POST("api/specialties")
    Call<BaseResponse<SpecialtyResponse>> createSpecialty(@Body SpecialtyRequest request);
    
    @PUT("api/specialties/{id}")
    Call<BaseResponse<SpecialtyResponse>> updateSpecialty(@Path("id") int specialtyId, @Body SpecialtyRequest request);
    
    @DELETE("api/specialties/{id}")
    Call<BaseResponse<Void>> deleteSpecialty(@Path("id") int specialtyId);

    // Admin Service Management
    @POST("api/services")
    Call<BaseResponse<ServiceResponse>> createService(@Body ServiceRequest request);
    
    @PUT("api/services/{id}")
    Call<BaseResponse<ServiceResponse>> updateService(@Path("id") int serviceId, @Body ServiceRequest request);
    
    @DELETE("api/services/{id}")
    Call<BaseResponse<Void>> deleteService(@Path("id") int serviceId);
    
    @PUT("api/services/{id}/price")
    Call<BaseResponse<ServiceResponse>> updateServicePrice(
            @Path("id") int serviceId,
            @Query("price") double price
    );

    // Admin User Management
    @GET("api/users")
    Call<BaseResponse<PaginatedResponse<UserResponse>>> getAllUsers(
            @Query("page") int page,
            @Query("size") int size
    );
    
    @PUT("api/users/{id}/lock")
    Call<BaseResponse<UserResponse>> lockUser(@Path("id") int userId);
    
    @PUT("api/users/{id}/unlock")
    Call<BaseResponse<UserResponse>> unlockUser(@Path("id") int userId);
    
    @PUT("api/users/{id}/role")
    Call<BaseResponse<UserResponse>> updateUserRole(
            @Path("id") int userId,
            @Query("role") String role
    );

    // Doctor Rating
    @POST("api/doctors/{id}/ratings")
    Call<BaseResponse<RatingResponse>> submitDoctorRating(
            @Path("id") int doctorId,
            @Body RatingRequest request
    );
    
    @GET("api/doctors/{id}/ratings")
    Call<BaseResponse<List<RatingResponse>>> getDoctorRatings(@Path("id") int doctorId);
    
    @GET("api/doctors/{id}/ratings/average")
    Call<BaseResponse<Double>> getDoctorAverageRating(@Path("id") int doctorId);

    // Notifications
    @GET("api/notifications/user")
    Call<BaseResponse<List<NotificationResponse>>> getUserNotifications();
    
    @GET("api/notifications/unread")
    Call<BaseResponse<List<NotificationResponse>>> getUnreadNotifications();
    
    @PUT("api/notifications/{id}/read")
    Call<BaseResponse<NotificationResponse>> markNotificationAsRead(@Path("id") int notificationId);
    
    @PUT("api/notifications/read-all")
    Call<BaseResponse<Void>> markAllNotificationsAsRead();
    
    @DELETE("api/notifications/{id}")
    Call<BaseResponse<Void>> deleteNotification(@Path("id") int notificationId);
} 