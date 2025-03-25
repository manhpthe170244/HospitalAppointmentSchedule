using System.Collections.Generic;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class ReservationTrendsDto
    {
        public Dictionary<string, int> DailyReservations { get; set; }
        public int TotalReservations { get; set; }
        public double GrowthRate { get; set; }
    }
} 