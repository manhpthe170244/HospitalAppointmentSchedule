using HospitalAppointmentShedule.Services.DTOs;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IStatisticsService
    {
        Task<ResultDto<DashboardStatisticsDto>> GetDashboardStatisticsAsync();
        Task<ResultDto<Dictionary<string, int>>> GetReservationsByStatusAsync();
        Task<ResultDto<Dictionary<string, int>>> GetReservationsBySpecialtyAsync();
        Task<ResultDto<Dictionary<string, decimal>>> GetRevenueBySpecialtyAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<List<RevenueByDateDto>>> GetRevenueByDateAsync(DateTime startDate, DateTime endDate, string groupBy = "day");
        Task<ResultDto<Dictionary<string, int>>> GetPatientsBySpecialtyAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<List<DoctorPerformanceDto>>> GetDoctorPerformanceAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<ReservationTrendsDto>> GetReservationTrendsAsync(int days);
        Task<ResultDto<List<TopServiceDto>>> GetTopServicesAsync(DateTime startDate, DateTime endDate, int limit = 5);
       // Task<ResultDto<Dictionary<string, int>>> GetPatientsCountBySpecialtyAsync(DateTime startDate, DateTime endDate); // Ensure this method is defined
    }
}