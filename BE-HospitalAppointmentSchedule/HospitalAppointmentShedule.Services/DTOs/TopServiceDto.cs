using System;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class TopServiceDto
    {
        public int ServiceId { get; set; }
        public string ServiceName { get; set; }
        public int Count { get; set; }
        public decimal Revenue { get; set; }
    }
} 