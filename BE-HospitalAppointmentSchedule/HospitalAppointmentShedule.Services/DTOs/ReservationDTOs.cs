using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class ReservationDto
    {
        public int ReservationId { get; set; }
        public int PatientId { get; set; }
        public string PatientName { get; set; } = null!;
        public DateTime AppointmentDate { get; set; }
        public string Status { get; set; } = null!;
        public string? CancellationReason { get; set; }
        public DateTime UpdatedDate { get; set; }
        public string? Symptoms { get; set; }
        public string? PriorExaminationImg { get; set; }
        public string ServiceName { get; set; } = null!;
        public string DoctorName { get; set; } = null!;
        public string SlotTime { get; set; } = null!;
        public string RoomName { get; set; } = null!;
        public bool HasPaid { get; set; }
    }

    public class ReservationDetailDto : ReservationDto
    {
        public PatientDto Patient { get; set; } = null!;
        public List<DoctorScheduleDto> DoctorSchedules { get; set; } = new List<DoctorScheduleDto>();
        public List<PaymentDto> Payments { get; set; } = new List<PaymentDto>();
        public List<MedicalRecordDto> MedicalRecords { get; set; } = new List<MedicalRecordDto>();
        public FeedbackDto? Feedback { get; set; }
    }

    public class ReservationCreateDto
    {
        [Required]
        public int PatientId { get; set; }

        [Required]
        public DateTime AppointmentDate { get; set; }

        public string? Symptoms { get; set; }

        public string? PriorExaminationImg { get; set; }

        [Required]
        public int DoctorScheduleId { get; set; }
    }

    public class ReservationUpdateDto
    {
        public DateTime? AppointmentDate { get; set; }

        [StringLength(20)]
        public string? Status { get; set; }

        [StringLength(255)]
        public string? CancellationReason { get; set; }

        public string? Symptoms { get; set; }

        public string? PriorExaminationImg { get; set; }

        public int? DoctorScheduleId { get; set; }
    }

    public class PatientDto
    {
        public int PatientId { get; set; }
        public string UserName { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public DateTime? DateOfBirth { get; set; }
        public string? Gender { get; set; }
        public string? Address { get; set; }
        public string? BloodType { get; set; }
        public string? Allergies { get; set; }
        public string? Rank { get; set; }
        public int? GuardianId { get; set; }
        public string? GuardianName { get; set; }
    }

    public class PatientCreateDto
    {
        public UserCreateDto User { get; set; } = null!;
        public string? BloodType { get; set; }
        public string? Allergies { get; set; }
        public int? GuardianId { get; set; }
    }

    public class MedicalRecordDto
    {
        public int MedicalRecordId { get; set; }
        public int ReservationId { get; set; }
        public string Diagnosis { get; set; } = null!;
        public string Treatment { get; set; } = null!;
        public string? Prescription { get; set; }
        public string? Notes { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime? FollowUpDate { get; set; }
    }

    public class MedicalRecordCreateDto
    {
        [Required]
        public int ReservationId { get; set; }

        [Required]
        public string Diagnosis { get; set; } = null!;

        [Required]
        public string Treatment { get; set; } = null!;

        public string? Prescription { get; set; }

        public string? Notes { get; set; }

        public DateTime? FollowUpDate { get; set; }
    }

    public class PaymentDto
    {
        public int PaymentId { get; set; }
        public int ReservationId { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; } = null!;
        public decimal Amount { get; set; }
        public string PaymentMethod { get; set; } = null!;
        public string PaymentStatus { get; set; } = null!;
        public DateTime PaymentDate { get; set; }
        public string? TransactionId { get; set; }
        public int? ReceptionistId { get; set; }
        public string? ReceptionistName { get; set; }
    }

    public class PaymentCreateDto
    {
        [Required]
        public int ReservationId { get; set; }

        [Required]
        public int UserId { get; set; }

        [Required]
        [Range(0, double.MaxValue)]
        public decimal Amount { get; set; }

        [Required]
        [StringLength(50)]
        public string PaymentMethod { get; set; } = null!;

        [StringLength(100)]
        public string? TransactionId { get; set; }

        public int? ReceptionistId { get; set; }
    }

    public class FeedbackDto
    {
        public int FeedbackId { get; set; }
        public int ReservationId { get; set; }
        public int Rating { get; set; }
        public string? Comment { get; set; }
        public DateTime FeedbackDate { get; set; }
    }

    public class FeedbackCreateDto
    {
        [Required]
        public int ReservationId { get; set; }

        [Required]
        [Range(1, 5)]
        public int Rating { get; set; }

        public string? Comment { get; set; }
    }
} 