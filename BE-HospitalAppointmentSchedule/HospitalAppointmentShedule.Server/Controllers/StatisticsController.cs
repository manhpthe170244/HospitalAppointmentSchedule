using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    [Authorize(Roles = "Admin")]
    public class StatisticsController : BaseApiController
    {
        private readonly IStatisticsService _statisticsService;

        public StatisticsController(IStatisticsService statisticsService)
        {
            _statisticsService = statisticsService;
        }

        [HttpGet("dashboard")]
        public async Task<IActionResult> GetDashboardStatistics()
        {
            var result = await _statisticsService.GetDashboardStatisticsAsync();
            return HandleResult(result);
        }

        // [HttpGet("reservations-by-status")]
        // public async Task<IActionResult> GetReservationsByStatus()
        // {
        //     var result = await _statisticsService.GetReservationsCountByStatusAsync();
        //     return HandleResult(result);
        // }

        // [HttpGet("reservations-by-specialty")]
        // public async Task<IActionResult> GetReservationsBySpecialty()
        // {
        //     var result = await _statisticsService.GetReservationsCountBySpecialtyAsync();
        //     return HandleResult(result);
        // }

        [HttpGet("revenue-by-specialty")]
        public async Task<IActionResult> GetRevenueBySpecialty([FromQuery] DateTime startDate, [FromQuery] DateTime endDate)
        {
            var result = await _statisticsService.GetRevenueBySpecialtyAsync(startDate, endDate);
            return HandleResult(result);
        }

        // [HttpGet("revenue-by-date")]
        // public async Task<IActionResult> GetRevenueByDate([FromQuery] DateTime startDate, [FromQuery] DateTime endDate, [FromQuery] string groupBy = "day")
        // {
        //     var result = await _statisticsService.GetRevenueByDateRangeAsync(startDate, endDate, groupBy);
        //     return HandleResult(result);
        // }

        // [HttpGet("patients-by-specialty")]
        // public async Task<IActionResult> GetPatientsBySpecialty([FromQuery] DateTime startDate, [FromQuery] DateTime endDate)
        // {
        //     var result = await _statisticsService.GetPatientsCountBySpecialtyAsync(startDate, endDate);
        //     return HandleResult(result);
        // }

        [HttpGet("doctor-performance")]
        public async Task<IActionResult> GetDoctorPerformance([FromQuery] DateTime startDate, [FromQuery] DateTime endDate)
        {
            var result = await _statisticsService.GetDoctorPerformanceAsync(startDate, endDate);
            return HandleResult(result);
        }
    }
} 