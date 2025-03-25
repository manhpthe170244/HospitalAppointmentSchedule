using System;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class DoctorPerformanceDto
    {
        public int DoctorId { get; set; }
        public string DoctorName { get; set; }
        public int CompletedAppointments { get; set; }
        public decimal Revenue { get; set; }
        public int PatientCount { get; set; }
        public double AverageRating { get; set; }
        public int FeedbackCount { get; set; }
    }
} 