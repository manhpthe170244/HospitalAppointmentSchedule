using HospitalAppointmentShedule.Services.DTOs.SpecialtyDTOs;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class ServiceDTO
    {
        public int ServiceId { get; set; }
        public string ServiceName { get; set; }
        public string? Overview { get; set; }
        public string? Process { get; set; }
        public string? TreatmentTechniques { get; set; }
        public decimal Price { get; set; }
        public TimeOnly? EstimatedTime { get; set; }
        public bool? IsPrepayment { get; set; }
        public int? ParentServiceId { get; set; }
        public int SpecialtyId { get; set; }
        public string? Image { get; set; }
        public ServiceDTO? ParentService { get; set; }
        public SpecialtyDTO? Specialty { get; set; }
    }
} 