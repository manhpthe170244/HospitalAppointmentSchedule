using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class DoctorDto
    {
        public int DoctorId { get; set; }
        public string UserName { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public string? AvatarUrl { get; set; }
        public string? Degree { get; set; }
        public string? AcademicTitle { get; set; }
        public int? Experience { get; set; }
        public string? Biography { get; set; }
        public List<string> Specialties { get; set; } = new List<string>();
        public List<CertificationDto>? Certifications { get; set; }
    }

    public class DoctorDetailDto : DoctorDto
    {
        public List<ServiceDto> Services { get; set; } = new List<ServiceDto>();
        public List<DoctorScheduleDto> Schedules { get; set; } = new List<DoctorScheduleDto>();
    }

    public class DoctorCreateDto
    {
        [Required]
        public UserCreateDto User { get; set; } = null!;

        public string? Degree { get; set; }
        public string? AcademicTitle { get; set; }
        public int? Experience { get; set; }
        public string? Biography { get; set; }
        public List<int> SpecialtyIds { get; set; } = new List<int>();
        public List<int> ServiceIds { get; set; } = new List<int>();
    }

    public class DoctorUpdateDto
    {
        public UserUpdateDto? User { get; set; }
        public string? Degree { get; set; }
        public string? AcademicTitle { get; set; }
        public int? Experience { get; set; }
        public string? Biography { get; set; }
        public List<int>? SpecialtyIds { get; set; }
        public List<int>? ServiceIds { get; set; }
    }

    public class CertificationDto
    {
        public int CertificationId { get; set; }
        public string CertificationUrl { get; set; } = null!;
        public string? Description { get; set; }
    }

    public class DoctorScheduleDto
    {
        public int DoctorScheduleId { get; set; }
        public int DoctorId { get; set; }
        public string DoctorName { get; set; } = null!;
        public int ServiceId { get; set; }
        public string ServiceName { get; set; } = null!;
        public int RoomId { get; set; }
        public string RoomName { get; set; } = null!;
        public int SlotId { get; set; }
        public TimeSpan SlotStartTime { get; set; }
        public TimeSpan SlotEndTime { get; set; }
        public string DayOfWeek { get; set; } = null!;
        public bool IsAvailable { get; set; }
    }

    public class DoctorScheduleCreateDto
    {
        [Required]
        public int DoctorId { get; set; }

        [Required]
        public int ServiceId { get; set; }

        [Required]
        public int RoomId { get; set; }

        [Required]
        public int SlotId { get; set; }

        [Required]
        [StringLength(10)]
        public string DayOfWeek { get; set; } = null!;
    }
} 