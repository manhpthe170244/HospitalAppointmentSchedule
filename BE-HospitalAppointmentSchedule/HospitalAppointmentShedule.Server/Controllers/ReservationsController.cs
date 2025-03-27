using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    public class ReservationsController : BaseApiController
    {
        private readonly IReservationService _reservationService;

        public ReservationsController(IReservationService reservationService)
        {
            _reservationService = reservationService;
        }

        [HttpGet]
        [Authorize(Roles = "Admin,Receptionist")]
        public async Task<IActionResult> GetReservations([FromQuery] int pageIndex = 1, [FromQuery] int pageSize = 10, [FromQuery] string? searchTerm = null)
        {
            var result = await _reservationService.GetPaginatedReservationsAsync(pageIndex, pageSize, searchTerm);
            return HandlePagedResult(result);
        }

        [HttpGet("{id}")]
        [Authorize(Roles = "Admin,Receptionist")]
        public async Task<IActionResult> GetReservation(int id)
        {
            var result = await _reservationService.GetReservationByIdAsync(id);
            return HandleResult(result);
        }

        [HttpGet("{id}/details")]
        [Authorize]
        public async Task<IActionResult> GetReservationDetails(int id)
        {
            var result = await _reservationService.GetReservationDetailsAsync(id);
            return HandleResult(result);
        }

        [HttpGet("patient/{patientId}")]
        [Authorize]
        public async Task<IActionResult> GetReservationsByPatient(int patientId)
        {
            var result = await _reservationService.GetReservationsByPatientAsync(patientId);
            return HandleResult(result);
        }

        [HttpGet("date-range")]
        [Authorize(Roles = "Admin,Receptionist,Doctor")]
        public async Task<IActionResult> GetReservationsByDateRange([FromQuery] DateTime startDate, [FromQuery] DateTime endDate)
        {
            var result = await _reservationService.GetReservationsByDateRangeAsync(startDate, endDate);
            return HandleResult(result);
        }

        [HttpGet("status/{status}")]
        [Authorize(Roles = "Admin,Receptionist,Doctor")]
        public async Task<IActionResult> GetReservationsByStatus(string status)
        {
            var result = await _reservationService.GetReservationsByStatusAsync(status);
            return HandleResult(result);
        }

        [HttpGet("specialty/{specialtyId}")]
        [Authorize(Roles = "Admin,Receptionist,Doctor")]
        public async Task<IActionResult> GetReservationsBySpecialty(int specialtyId, [FromQuery] DateTime? date = null)
        {
            var result = await _reservationService.GetReservationsBySpecialtyAsync(specialtyId, date);
            return HandleResult(result);
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> CreateReservation(ReservationCreateDto reservationDto)
        {
            var result = await _reservationService.CreateReservationAsync(reservationDto);
            return HandleResult(result);
        }

        [HttpPut("{id}")]
        [Authorize(Roles = "Admin,Receptionist")]
        public async Task<IActionResult> UpdateReservation(int id, ReservationUpdateDto reservationDto)
        {
            var result = await _reservationService.UpdateReservationAsync(id, reservationDto);
            return HandleResult(result);
        }

        [HttpPut("{id}/cancel")]
        [Authorize]
        public async Task<IActionResult> CancelReservation(int id, [FromBody] string reason)
        {
            var result = await _reservationService.CancelReservationAsync(id, reason);
            return HandleResult(result);
        }

        [HttpPut("{id}/approve")]
        [Authorize(Roles = "Admin,Receptionist")]
        public async Task<IActionResult> ApproveReservation(int id)
        {
            var result = await _reservationService.ApproveReservationAsync(id);
            return HandleResult(result);
        }

        [HttpPut("{id}/complete")]
        [Authorize(Roles = "Admin,Doctor")]
        public async Task<IActionResult> CompleteReservation(int id)
        {
            var result = await _reservationService.CompleteReservationAsync(id);
            return HandleResult(result);
        }

        [HttpPost("check-slot-availability")]
        [AllowAnonymous]
        public async Task<IActionResult> CheckSlotAvailability([FromQuery] int doctorScheduleId, [FromQuery] DateTime appointmentDate)
        {
            var result = await _reservationService.CheckSlotAvailabilityAsync(doctorScheduleId, appointmentDate);
            return HandleResult(result);
        }

        [HttpPost("payments")]
        [Authorize]
        public async Task<IActionResult> CreatePayment(PaymentCreateDto paymentDto)
        {
            var result = await _reservationService.CreatePaymentAsync(paymentDto);
            return HandleResult(result);
        }

        [HttpGet("{id}/payments")]
        [Authorize]
        public async Task<IActionResult> GetPaymentsByReservation(int id)
        {
            var result = await _reservationService.GetPaymentsByReservationAsync(id);
            return HandleResult(result);
        }

        [HttpPost("medical-records")]
        [Authorize(Roles = "Admin,Doctor")]
        public async Task<IActionResult> CreateMedicalRecord(MedicalRecordCreateDto medicalRecordDto)
        {
            var result = await _reservationService.CreateMedicalRecordAsync(medicalRecordDto);
            return HandleResult(result);
        }

        [HttpGet("patient/{patientId}/medical-records")]
        [Authorize]
        public async Task<IActionResult> GetMedicalRecordsByPatient(int patientId)
        {
            var result = await _reservationService.GetMedicalRecordsByPatientAsync(patientId);
            return HandleResult(result);
        }

        [HttpPost("feedbacks")]
        [Authorize]
        public async Task<IActionResult> CreateFeedback(FeedbackCreateDto feedbackDto)
        {
            var result = await _reservationService.CreateFeedbackAsync(feedbackDto);
            return HandleResult(result);
        }
    }
} 