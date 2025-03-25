using System;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class RevenueByDateDto
    {
        public string Period { get; set; }
        public decimal Revenue { get; set; }
        public int Count { get; set; }
    }
} 