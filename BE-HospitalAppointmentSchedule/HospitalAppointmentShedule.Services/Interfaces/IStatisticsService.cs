using HospitalAppointmentShedule.Services.DTOs;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IStatisticsService
    {
        Task<ResultDto<Dictionary<string, int>>> GetReservationsCountByStatusAsync();
        Task<ResultDto<Dictionary<string, int>>> GetReservationsCountBySpecialtyAsync();
        Task<ResultDto<Dictionary<string, decimal>>> GetRevenueBySpecialtyAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<Dictionary<string, decimal>>> GetRevenueByDateRangeAsync(DateTime startDate, DateTime endDate, string groupBy = "day");
        Task<ResultDto<Dictionary<string, int>>> GetPatientsCountBySpecialtyAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<Dictionary<string, int>>> GetDoctorPerformanceAsync(DateTime startDate, DateTime endDate);
        Task<ResultDto<Dictionary<string, object>>> GetDashboardStatisticsAsync();
    }
} 