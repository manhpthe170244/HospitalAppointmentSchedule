using System;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class DashboardStatisticsDto
    {
        public int TotalUsers { get; set; }
        public int TotalDoctors { get; set; }
        public int TotalPatients { get; set; }
        public int TotalReservations { get; set; }
        public int TotalServices { get; set; }
        public int TotalSpecialties { get; set; }
        public int PendingReservations { get; set; }
        public int ConfirmedReservations { get; set; }
        public int CompletedReservations { get; set; }
        public int CancelledReservations { get; set; }
        public int PaidReservations { get; set; }
        public int TodayAppointments { get; set; }
        public decimal LastMonthRevenue { get; set; }
        public int UpcomingAppointments { get; set; }
    }
} 