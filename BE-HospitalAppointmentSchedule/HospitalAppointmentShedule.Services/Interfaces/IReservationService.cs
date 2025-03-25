using HospitalAppointmentShedule.Services.DTOs;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IReservationService
    {
        Task<ResultDto<ReservationDto>> GetReservationByIdAsync(int reservationId);
        Task<ResultDto<ReservationDetailDto>> GetReservationDetailsAsync(int reservationId);
        Task<ResultDto<List<ReservationDto>>> GetAllReservationsAsync();
        Task<ResultDto<PaginatedResultDto<ReservationDto>>> GetPaginatedReservationsAsync(int pageIndex, int pageSize, string? searchTerm = null);
        Task<ResultDto<List<ReservationDto>>> GetReservationsByPatientAsync(int patientId);
        Task<ResultDto<List<ReservationDto>>> GetReservationsByDateRangeAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<List<ReservationDto>>> GetReservationsByStatusAsync(string status);
        Task<ResultDto<List<ReservationDto>>> GetReservationsBySpecialtyAsync(int specialtyId, DateTime? date = null);
        Task<ResultDto<ReservationDto>> CreateReservationAsync(ReservationCreateDto reservationDto);
        Task<ResultDto<ReservationDto>> UpdateReservationAsync(int reservationId, ReservationUpdateDto reservationDto);
        Task<ResultDto<bool>> CancelReservationAsync(int reservationId, string reason);
        Task<ResultDto<bool>> ApproveReservationAsync(int reservationId);
        Task<ResultDto<bool>> CompleteReservationAsync(int reservationId);
        Task<ResultDto<PaymentDto>> CreatePaymentAsync(PaymentCreateDto paymentDto);
        Task<ResultDto<List<PaymentDto>>> GetPaymentsByReservationAsync(int reservationId);
        Task<ResultDto<MedicalRecordDto>> CreateMedicalRecordAsync(MedicalRecordCreateDto medicalRecordDto);
        Task<ResultDto<List<MedicalRecordDto>>> GetMedicalRecordsByPatientAsync(int patientId);
        Task<ResultDto<FeedbackDto>> CreateFeedbackAsync(FeedbackCreateDto feedbackDto);
        Task<ResultDto<bool>> CheckSlotAvailabilityAsync(int doctorScheduleId, DateTime appointmentDate);
        Task<ResultDto<List<PatientDto>>> GetAllPatientsAsync();
        Task<ResultDto<PatientDto>> CreatePatientAsync(PatientCreateDto patientDto);
        Task<ResultDto<PatientDto>> GetPatientByIdAsync(int patientId);
    }
} 