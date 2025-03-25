using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Services
{
    public class ReservationService : IReservationService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IMapper _mapper;
        private readonly IUserService _userService;

        public ReservationService(IUnitOfWork unitOfWork, IMapper mapper, IUserService userService)
        {
            _unitOfWork = unitOfWork;
            _mapper = mapper;
            _userService = userService;
        }

        #region Reservation Methods
        public async Task<ResultDto<ReservationDto>> GetReservationByIdAsync(int reservationId)
        {
            var reservation = await _unitOfWork.Reservations.GetReservationWithDetailsAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<ReservationDto>.Failure("Reservation not found");
            
            var reservationDto = _mapper.Map<ReservationDto>(reservation);
            return ResultDto<ReservationDto>.Success(reservationDto);
        }

        public async Task<ResultDto<ReservationDetailDto>> GetReservationDetailsAsync(int reservationId)
        {
            var reservation = await _unitOfWork.Reservations.GetReservationWithDetailsAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<ReservationDetailDto>.Failure("Reservation not found");
            
            var reservationDetailDto = _mapper.Map<ReservationDetailDto>(reservation);
            return ResultDto<ReservationDetailDto>.Success(reservationDetailDto);
        }

        public async Task<ResultDto<List<ReservationDto>>> GetAllReservationsAsync()
        {
            var reservations = await _unitOfWork.Reservations.Query()
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Room)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Include(r => r.Payments)
                .ToListAsync();
            
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            return ResultDto<List<ReservationDto>>.Success(reservationDtos);
        }

        public async Task<ResultDto<PaginatedResultDto<ReservationDto>>> GetPaginatedReservationsAsync(int pageIndex, int pageSize, string? searchTerm = null)
        {
            var query = _unitOfWork.Reservations.Query()
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Include(r => r.Payments)
                .AsQueryable();
            
            if (!string.IsNullOrEmpty(searchTerm))
            {
                query = query.Where(r => 
                    r.Patient.PatientNavigation.UserName.Contains(searchTerm) || 
                    r.Patient.PatientNavigation.Phone.Contains(searchTerm) ||
                    r.Status.Contains(searchTerm) ||
                    r.DoctorSchedules.Any(ds => 
                        ds.Doctor.DoctorNavigation.UserName.Contains(searchTerm) ||
                        ds.Service.ServiceName.Contains(searchTerm)
                    ));
            }
            
            var totalCount = await query.CountAsync();
            var reservations = await query
                .OrderByDescending(r => r.UpdatedDate)
                .Skip((pageIndex - 1) * pageSize)
                .Take(pageSize)
                .ToListAsync();
            
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            
            var paginatedResult = PaginatedResultDto<ReservationDto>.Create(
                reservationDtos,
                totalCount,
                pageIndex,
                pageSize);
            
            return ResultDto<PaginatedResultDto<ReservationDto>>.Success(paginatedResult);
        }

        public async Task<ResultDto<List<ReservationDto>>> GetReservationsByPatientAsync(int patientId)
        {
            var reservations = await _unitOfWork.Reservations.GetReservationsByPatientAsync(patientId);
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            return ResultDto<List<ReservationDto>>.Success(reservationDtos);
        }

        public async Task<ResultDto<List<ReservationDto>>> GetReservationsByDateRangeAsync(DateTime startDate, DateTime endDate)
        {
            var reservations = await _unitOfWork.Reservations.GetReservationsByDateRangeAsync(startDate, endDate);
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            return ResultDto<List<ReservationDto>>.Success(reservationDtos);
        }

        public async Task<ResultDto<List<ReservationDto>>> GetReservationsByStatusAsync(string status)
        {
            var reservations = await _unitOfWork.Reservations.GetReservationsByStatusAsync(status);
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            return ResultDto<List<ReservationDto>>.Success(reservationDtos);
        }

        public async Task<ResultDto<List<ReservationDto>>> GetReservationsBySpecialtyAsync(int specialtyId, DateTime? date = null)
        {
            var reservations = await _unitOfWork.Reservations.GetReservationsBySpecialtyAsync(specialtyId, date);
            var reservationDtos = _mapper.Map<List<ReservationDto>>(reservations);
            return ResultDto<List<ReservationDto>>.Success(reservationDtos);
        }

        public async Task<ResultDto<ReservationDto>> CreateReservationAsync(ReservationCreateDto reservationDto)
        {
            // Validate patient exists
            var patient = await _unitOfWork.Repository<Patient>().GetByIdAsync(reservationDto.PatientId);
            if (patient == null)
                return ResultDto<ReservationDto>.Failure("Patient not found");
            
            // Validate doctor schedule exists
            var doctorSchedule = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .Include(ds => ds.Doctor)
                .Include(ds => ds.Service)
                .Include(ds => ds.Room)
                .Include(ds => ds.Slot)
                .FirstOrDefaultAsync(ds => ds.DoctorScheduleId == reservationDto.DoctorScheduleId);
            
            if (doctorSchedule == null)
                return ResultDto<ReservationDto>.Failure("Doctor schedule not found");
            
            // Check if the appointment date matches the schedule day
            if (doctorSchedule.DayOfWeek != reservationDto.AppointmentDate.DayOfWeek.ToString())
                return ResultDto<ReservationDto>.Failure("Appointment day does not match the doctor's schedule day");
            
            // Check if the time slot is available
            var isSlotAvailable = await _unitOfWork.Reservations.IsSlotAvailableAsync(
                reservationDto.DoctorScheduleId, 
                reservationDto.AppointmentDate);
            
            if (!isSlotAvailable)
                return ResultDto<ReservationDto>.Failure("This time slot is already booked");
            
            // Create the reservation
            var reservation = _mapper.Map<Reservation>(reservationDto);
            reservation.Status = "Chờ xác nhận"; // Pending confirmation
            reservation.UpdatedDate = DateTime.Now;
            
            await _unitOfWork.Reservations.AddAsync(reservation);
            await _unitOfWork.SaveChangesAsync();
            
            // Associate with doctor schedule
            var reservationDoctorSchedule = new Dictionary<string, object>
            {
                { "ReservationId", reservation.ReservationId },
                { "DoctorScheduleId", reservationDto.DoctorScheduleId }
            };
            
            // Save the relationship
            await _unitOfWork.Repository<Dictionary<string, object>>().AddAsync(reservationDoctorSchedule);
            await _unitOfWork.SaveChangesAsync();
            
            // Get the complete reservation with details
            var createdReservation = await _unitOfWork.Reservations.GetReservationWithDetailsAsync(reservation.ReservationId);
            var reservationResponseDto = _mapper.Map<ReservationDto>(createdReservation);
            
            return ResultDto<ReservationDto>.Success(reservationResponseDto, "Reservation created successfully");
        }

        public async Task<ResultDto<ReservationDto>> UpdateReservationAsync(int reservationId, ReservationUpdateDto reservationDto)
        {
            var reservation = await _unitOfWork.Reservations.GetReservationWithDetailsAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<ReservationDto>.Failure("Reservation not found");
            
            // Check if we can update (not completed or cancelled)
            if (reservation.Status == "Đã hoàn thành" || reservation.Status == "Đã hủy")
                return ResultDto<ReservationDto>.Failure("Cannot update a completed or cancelled reservation");
            
            // Handle doctor schedule change if requested
            if (reservationDto.DoctorScheduleId.HasValue && 
                !reservation.DoctorSchedules.Any(ds => ds.DoctorScheduleId == reservationDto.DoctorScheduleId.Value))
            {
                // Validate doctor schedule exists
                var doctorSchedule = await _unitOfWork.Repository<DoctorSchedule>().Query()
                    .Include(ds => ds.Doctor)
                    .Include(ds => ds.Service)
                    .Include(ds => ds.Room)
                    .Include(ds => ds.Slot)
                    .FirstOrDefaultAsync(ds => ds.DoctorScheduleId == reservationDto.DoctorScheduleId.Value);
                
                if (doctorSchedule == null)
                    return ResultDto<ReservationDto>.Failure("Doctor schedule not found");
                
                // Check appointment date day matches schedule day
                var appointmentDate = reservationDto.AppointmentDate ?? reservation.AppointmentDate.Value;
                if (doctorSchedule.DayOfWeek != appointmentDate.DayOfWeek.ToString())
                    return ResultDto<ReservationDto>.Failure("Appointment day does not match the doctor's schedule day");
                
                // Check if the time slot is available
                var isSlotAvailable = await _unitOfWork.Reservations.IsSlotAvailableAsync(
                    reservationDto.DoctorScheduleId.Value, 
                    appointmentDate);
                
                if (!isSlotAvailable)
                    return ResultDto<ReservationDto>.Failure("This time slot is already booked");
                
                // Remove existing schedule relationships
                var existingRelationships = await _unitOfWork.Repository<Dictionary<string, object>>().Query()
                    .Where(rds => rds["ReservationId"].Equals(reservationId))
                    .ToListAsync();
                
                foreach (var relationship in existingRelationships)
                {
                    _unitOfWork.Repository<Dictionary<string, object>>().Delete(relationship);
                }
                
                // Add new relationship
                var newRelationship = new Dictionary<string, object>
                {
                    { "ReservationId", reservationId },
                    { "DoctorScheduleId", reservationDto.DoctorScheduleId.Value }
                };
                
                await _unitOfWork.Repository<Dictionary<string, object>>().AddAsync(newRelationship);
            }
            
            // Update properties
            if (reservationDto.AppointmentDate.HasValue)
                reservation.AppointmentDate = reservationDto.AppointmentDate.Value;
                
            if (reservationDto.Status != null)
                reservation.Status = reservationDto.Status;
                
            if (reservationDto.CancellationReason != null)
                reservation.CancellationReason = reservationDto.CancellationReason;
                
            if (reservationDto.Symptoms != null)
                reservation.Reason = reservationDto.Symptoms;
                
            if (reservationDto.PriorExaminationImg != null)
                reservation.PriorExaminationImg = reservationDto.PriorExaminationImg;
            
            reservation.UpdatedDate = DateTime.Now;
            
            _unitOfWork.Reservations.Update(reservation);
            await _unitOfWork.SaveChangesAsync();
            
            // Get updated reservation
            var updatedReservation = await _unitOfWork.Reservations.GetReservationWithDetailsAsync(reservationId);
            var reservationResponseDto = _mapper.Map<ReservationDto>(updatedReservation);
            
            return ResultDto<ReservationDto>.Success(reservationResponseDto, "Reservation updated successfully");
        }

        public async Task<ResultDto<bool>> CancelReservationAsync(int reservationId, string reason)
        {
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<bool>.Failure("Reservation not found");
            
            // Check if we can cancel (not completed or already cancelled)
            if (reservation.Status == "Đã hoàn thành")
                return ResultDto<bool>.Failure("Cannot cancel a completed reservation");
                
            if (reservation.Status == "Đã hủy")
                return ResultDto<bool>.Failure("Reservation is already cancelled");
            
            // Update status and reason
            reservation.Status = "Đã hủy"; // Cancelled
            reservation.CancellationReason = reason;
            reservation.UpdatedDate = DateTime.Now;
            
            _unitOfWork.Reservations.Update(reservation);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Reservation cancelled successfully");
        }

        public async Task<ResultDto<bool>> ApproveReservationAsync(int reservationId)
        {
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<bool>.Failure("Reservation not found");
            
            // Check if we can approve (pending confirmation)
            if (reservation.Status != "Chờ xác nhận")
                return ResultDto<bool>.Failure("Reservation is not in a pending state");
            
            // Update status
            reservation.Status = "Đã xác nhận"; // Confirmed
            reservation.UpdatedDate = DateTime.Now;
            
            _unitOfWork.Reservations.Update(reservation);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Reservation approved successfully");
        }

        public async Task<ResultDto<bool>> CompleteReservationAsync(int reservationId)
        {
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(reservationId);
            
            if (reservation == null)
                return ResultDto<bool>.Failure("Reservation not found");
            
            // Check if we can complete (confirmed or paid)
            if (reservation.Status != "Đã xác nhận" && reservation.Status != "Đã thanh toán")
                return ResultDto<bool>.Failure("Reservation must be confirmed or paid before completion");
            
            // Update status
            reservation.Status = "Đã hoàn thành"; // Completed
            reservation.UpdatedDate = DateTime.Now;
            
            _unitOfWork.Reservations.Update(reservation);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Reservation completed successfully");
        }

        public async Task<ResultDto<bool>> CheckSlotAvailabilityAsync(int doctorScheduleId, DateTime appointmentDate)
        {
            // Validate doctor schedule exists
            var doctorSchedule = await _unitOfWork.Repository<DoctorSchedule>().GetByIdAsync(doctorScheduleId);
            if (doctorSchedule == null)
                return ResultDto<bool>.Failure("Doctor schedule not found");
            
            // Check appointment date day matches schedule day
            if (doctorSchedule.DayOfWeek != appointmentDate.DayOfWeek.ToString())
                return ResultDto<bool>.Failure("Appointment day does not match the doctor's schedule day");
            
            // Check availability
            var isAvailable = await _unitOfWork.Reservations.IsSlotAvailableAsync(doctorScheduleId, appointmentDate);
            
            return ResultDto<bool>.Success(isAvailable, isAvailable
                ? "Time slot is available"
                : "Time slot is already booked");
        }
        #endregion

        #region Payment Methods
        public async Task<ResultDto<PaymentDto>> CreatePaymentAsync(PaymentCreateDto paymentDto)
        {
            // Validate reservation exists
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(paymentDto.ReservationId);
            if (reservation == null)
                return ResultDto<PaymentDto>.Failure("Reservation not found");
            
            // Validate user exists
            var user = await _unitOfWork.Users.GetByIdAsync(paymentDto.UserId);
            if (user == null)
                return ResultDto<PaymentDto>.Failure("User not found");
            
            // Validate receptionist if provided
            if (paymentDto.ReceptionistId.HasValue)
            {
                var receptionist = await _unitOfWork.Repository<Receptionist>().GetByIdAsync(paymentDto.ReceptionistId.Value);
                if (receptionist == null)
                    return ResultDto<PaymentDto>.Failure("Receptionist not found");
            }
            
            // Create payment
            var payment = _mapper.Map<Payment>(paymentDto);
            payment.PaymentDate = DateTime.Now;
            payment.PaymentStatus = "Thành công"; // Success
            
            await _unitOfWork.Repository<Payment>().AddAsync(payment);
            
            // Update reservation status if pending
            if (reservation.Status == "Chờ xác nhận" || reservation.Status == "Đã xác nhận")
            {
                reservation.Status = "Đã thanh toán"; // Paid
                reservation.UpdatedDate = DateTime.Now;
                _unitOfWork.Reservations.Update(reservation);
            }
            
            await _unitOfWork.SaveChangesAsync();
            
            // Get complete payment with user and receptionist details
            var createdPayment = await _unitOfWork.Repository<Payment>().Query()
                .Include(p => p.User)
                .Include(p => p.Receptionist)
                    .ThenInclude(r => r != null ? r.ReceptionistNavigation : null)
                .FirstOrDefaultAsync(p => p.PaymentId == payment.PaymentId);
            
            var paymentResponseDto = _mapper.Map<PaymentDto>(createdPayment);
            
            return ResultDto<PaymentDto>.Success(paymentResponseDto, "Payment created successfully");
        }

        public async Task<ResultDto<List<PaymentDto>>> GetPaymentsByReservationAsync(int reservationId)
        {
            // Validate reservation exists
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(reservationId);
            if (reservation == null)
                return ResultDto<List<PaymentDto>>.Failure("Reservation not found");
            
            // Get payments
            var payments = await _unitOfWork.Repository<Payment>().Query()
                .Include(p => p.User)
                .Include(p => p.Receptionist)
                    .ThenInclude(r => r != null ? r.ReceptionistNavigation : null)
                .Where(p => p.ReservationId == reservationId)
                .ToListAsync();
            
            var paymentDtos = _mapper.Map<List<PaymentDto>>(payments);
            
            return ResultDto<List<PaymentDto>>.Success(paymentDtos);
        }
        #endregion

        #region Medical Record Methods
        public async Task<ResultDto<MedicalRecordDto>> CreateMedicalRecordAsync(MedicalRecordCreateDto medicalRecordDto)
        {
            // Validate reservation exists
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(medicalRecordDto.ReservationId);
            if (reservation == null)
                return ResultDto<MedicalRecordDto>.Failure("Reservation not found");
            
            // Check if the reservation is completed
            if (reservation.Status != "Đã hoàn thành")
                return ResultDto<MedicalRecordDto>.Failure("Can only create medical records for completed reservations");
            
            var medicalRecord = _mapper.Map<MedicalRecord>(medicalRecordDto);
            medicalRecord.CreatedAt = DateTime.Now;
            
            await _unitOfWork.Repository<MedicalRecord>().AddAsync(medicalRecord);
            await _unitOfWork.SaveChangesAsync();
            
            var medicalRecordResponseDto = _mapper.Map<MedicalRecordDto>(medicalRecord);
            
            return ResultDto<MedicalRecordDto>.Success(medicalRecordResponseDto, "Medical record created successfully");
        }

        public async Task<ResultDto<List<MedicalRecordDto>>> GetMedicalRecordsByPatientAsync(int patientId)
        {
            // Validate patient exists
            var patient = await _unitOfWork.Repository<Patient>().GetByIdAsync(patientId);
            if (patient == null)
                return ResultDto<List<MedicalRecordDto>>.Failure("Patient not found");
            
            // Get all reservations for the patient
            var reservationIds = await _unitOfWork.Reservations.Query()
                .Where(r => r.PatientId == patientId)
                .Select(r => r.ReservationId)
                .ToListAsync();
            
            // Get all medical records for these reservations
            var medicalRecords = await _unitOfWork.Repository<MedicalRecord>().Query()
                .Where(mr => reservationIds.Contains(mr.ReservationId))
                .ToListAsync();
            
            var medicalRecordDtos = _mapper.Map<List<MedicalRecordDto>>(medicalRecords);
            
            return ResultDto<List<MedicalRecordDto>>.Success(medicalRecordDtos);
        }
        #endregion

        #region Feedback Methods
        public async Task<ResultDto<FeedbackDto>> CreateFeedbackAsync(FeedbackCreateDto feedbackDto)
        {
            // Validate reservation exists
            var reservation = await _unitOfWork.Reservations.GetByIdAsync(feedbackDto.ReservationId);
            if (reservation == null)
                return ResultDto<FeedbackDto>.Failure("Reservation not found");
            
            // Check if the reservation is completed
            if (reservation.Status != "Đã hoàn thành")
                return ResultDto<FeedbackDto>.Failure("Can only provide feedback for completed reservations");
            
            // Check if feedback already exists
            var existingFeedback = await _unitOfWork.Repository<Feedback>().Query()
                .AnyAsync(f => f.ReservationId == feedbackDto.ReservationId);
            
            if (existingFeedback)
                return ResultDto<FeedbackDto>.Failure("Feedback already exists for this reservation");
            
            var feedback = _mapper.Map<Feedback>(feedbackDto);
            feedback.FeedbackDate = DateTime.Now;
            
            await _unitOfWork.Repository<Feedback>().AddAsync(feedback);
            await _unitOfWork.SaveChangesAsync();
            
            var feedbackResponseDto = _mapper.Map<FeedbackDto>(feedback);
            
            return ResultDto<FeedbackDto>.Success(feedbackResponseDto, "Feedback submitted successfully");
        }
        #endregion

        #region Patient Methods
        public async Task<ResultDto<List<PatientDto>>> GetAllPatientsAsync()
        {
            var patients = await _unitOfWork.Repository<Patient>().Query()
                .Include(p => p.PatientNavigation)
                .Include(p => p.Guardian)
                .ToListAsync();
            
            var patientDtos = _mapper.Map<List<PatientDto>>(patients);
            return ResultDto<List<PatientDto>>.Success(patientDtos);
        }

        public async Task<ResultDto<PatientDto>> GetPatientByIdAsync(int patientId)
        {
            var patient = await _unitOfWork.Repository<Patient>().Query()
                .Include(p => p.PatientNavigation)
                .Include(p => p.Guardian)
                .FirstOrDefaultAsync(p => p.PatientId == patientId);
            
            if (patient == null)
                return ResultDto<PatientDto>.Failure("Patient not found");
            
            var patientDto = _mapper.Map<PatientDto>(patient);
            return ResultDto<PatientDto>.Success(patientDto);
        }

        public async Task<ResultDto<PatientDto>> CreatePatientAsync(PatientCreateDto patientDto)
        {
            // First create user account
            var createUserResult = await _userService.CreateUserAsync(patientDto.User);
            
            if (!createUserResult.IsSuccess)
                return ResultDto<PatientDto>.Failure(createUserResult.Message, createUserResult.Errors);
            
            var user = await _unitOfWork.Users.GetByIdAsync(createUserResult.Data.UserId);
            
            // Assign patient role if not already assigned
            var patientRole = await _unitOfWork.Repository<Role>().Query()
                .FirstOrDefaultAsync(r => r.RoleName == "Patient");
            
            if (patientRole != null && !user.Roles.Any(r => r.RoleId == patientRole.RoleId))
            {
                user.Roles.Add(patientRole);
                await _unitOfWork.SaveChangesAsync();
            }
            
            // Validate guardian if provided
            if (patientDto.GuardianId.HasValue)
            {
                var guardian = await _unitOfWork.Users.GetByIdAsync(patientDto.GuardianId.Value);
                if (guardian == null)
                    return ResultDto<PatientDto>.Failure("Guardian not found");
            }
            
            // Create patient profile
            var patient = new Patient
            {
                PatientId = user.UserId,
                GuardianId = patientDto.GuardianId,
                Rank = "Regular" // Default rank
            };
            
            await _unitOfWork.Repository<Patient>().AddAsync(patient);
            await _unitOfWork.SaveChangesAsync();
            
            // Get complete patient with user details
            var createdPatient = await _unitOfWork.Repository<Patient>().Query()
                .Include(p => p.PatientNavigation)
                .Include(p => p.Guardian)
                .FirstOrDefaultAsync(p => p.PatientId == patient.PatientId);
            
            var patientResponseDto = _mapper.Map<PatientDto>(createdPatient);
            
            return ResultDto<PatientDto>.Success(patientResponseDto, "Patient created successfully");
        }
        #endregion
    }
} 