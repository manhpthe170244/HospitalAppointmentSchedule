using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class ServiceDto
    {
        public int ServiceId { get; set; }
        public string ServiceName { get; set; } = null!;
        public decimal Price { get; set; }
        public string? Overview { get; set; }
        public int SpecialtyId { get; set; }
        public string SpecialtyName { get; set; } = null!;
        public bool IsPrepayment { get; set; }
        public int? ParentServiceId { get; set; }
        public string? ParentServiceName { get; set; }
    }

    public class ServiceDetailDto : ServiceDto
    {
        public List<ServiceDto> ChildServices { get; set; } = new List<ServiceDto>();
        public List<DeviceDto> Devices { get; set; } = new List<DeviceDto>();
        public List<DoctorDto> Doctors { get; set; } = new List<DoctorDto>();
    }

    public class ServiceCreateDto
    {
        [Required]
        [StringLength(100)]
        public string ServiceName { get; set; } = null!;

        [Required]
        [Range(0, double.MaxValue)]
        public decimal Price { get; set; }

        [StringLength(500)]
        public string? Overview { get; set; }

        [Required]
        public int SpecialtyId { get; set; }

        public bool IsPrepayment { get; set; }

        public int? ParentServiceId { get; set; }

        public List<int>? DeviceIds { get; set; }
    }

    public class ServiceUpdateDto
    {
        [StringLength(100)]
        public string? ServiceName { get; set; }

        [Range(0, double.MaxValue)]
        public decimal? Price { get; set; }

        [StringLength(500)]
        public string? Overview { get; set; }

        public int? SpecialtyId { get; set; }

        public bool? IsPrepayment { get; set; }

        public int? ParentServiceId { get; set; }

        public List<int>? DeviceIds { get; set; }
    }

    public class DeviceDto
    {
        public int DeviceId { get; set; }
        public string Name { get; set; } = null!;
        public string? Description { get; set; }
    }

    public class SpecialtyDto
    {
        public int SpecialtyId { get; set; }
        public string SpecialtyName { get; set; } = null!;
        public string? Description { get; set; }
        public string? Image { get; set; }
    }

    public class SpecialtyCreateDto
    {
        [Required]
        [StringLength(100)]
        public string SpecialtyName { get; set; } = null!;

        public string? Description { get; set; }

        public string? Image { get; set; }
    }

    public class SpecialtyUpdateDto
    {
        [StringLength(100)]
        public string? SpecialtyName { get; set; }

        public string? Description { get; set; }

        public string? Image { get; set; }
    }
} 