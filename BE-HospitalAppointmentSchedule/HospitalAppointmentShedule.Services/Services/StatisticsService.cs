using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Services
{
    public class StatisticsService : IStatisticsService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IMapper _mapper;

        public StatisticsService(IUnitOfWork unitOfWork, IMapper mapper)
        {
            _unitOfWork = unitOfWork;
            _mapper = mapper;
        }

        public async Task<ResultDto<DashboardStatisticsDto>> GetDashboardStatisticsAsync()
        {
            try
            {
                // Get total counts
                var totalUsers = await _unitOfWork.Users.Query().CountAsync();
                var totalDoctors = await _unitOfWork.Repository<Doctor>().Query().CountAsync();
                var totalPatients = await _unitOfWork.Repository<Patient>().Query().CountAsync();
                var totalReservations = await _unitOfWork.Reservations.Query().CountAsync();
                var totalServices = await _unitOfWork.Repository<Service>().Query().CountAsync();
                var totalSpecialties = await _unitOfWork.Repository<Specialty>().Query().CountAsync();
                
                // Get reservations by status
                var pendingReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.Status == "Chờ xác nhận")
                    .CountAsync();
                    
                var confirmedReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.Status == "Đã xác nhận")
                    .CountAsync();
                    
                var completedReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.Status == "Đã hoàn thành")
                    .CountAsync();
                    
                var cancelledReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.Status == "Đã hủy")
                    .CountAsync();
                    
                var paidReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.Status == "Đã thanh toán")
                    .CountAsync();
                
                // Get today's appointments
                var today = DateTime.Today;
                var todayAppointments = await _unitOfWork.Reservations.Query()
                    .Where(r => r.AppointmentDate.Date == today)
                    .CountAsync();
                
                // Get revenue from last 30 days
                var lastMonthDate = DateTime.Today.AddDays(-30);
                var lastMonthRevenue = await _unitOfWork.Repository<Payment>().Query()
                    .Where(p => p.PaymentDate >= lastMonthDate && p.PaymentStatus == "Thành công")
                    .SumAsync(p => p.Amount);
                
                // Get appointments for next 7 days
                var nextWeekDate = DateTime.Today.AddDays(7);
                var upcomingAppointments = await _unitOfWork.Reservations.Query()
                    .Where(r => r.AppointmentDate.Date > today && r.AppointmentDate.Date <= nextWeekDate)
                    .CountAsync();
                
                var dashboardStats = new DashboardStatisticsDto
                {
                    TotalUsers = totalUsers,
                    TotalDoctors = totalDoctors,
                    TotalPatients = totalPatients,
                    TotalReservations = totalReservations,
                    TotalServices = totalServices,
                    TotalSpecialties = totalSpecialties,
                    PendingReservations = pendingReservations,
                    ConfirmedReservations = confirmedReservations,
                    CompletedReservations = completedReservations,
                    CancelledReservations = cancelledReservations,
                    PaidReservations = paidReservations,
                    TodayAppointments = todayAppointments,
                    LastMonthRevenue = lastMonthRevenue,
                    UpcomingAppointments = upcomingAppointments
                };
                
                return ResultDto<DashboardStatisticsDto>.Success(dashboardStats);
            }
            catch (Exception ex)
            {
                return ResultDto<DashboardStatisticsDto>.Failure("Failed to get dashboard statistics: " + ex.Message);
            }
        }

        public async Task<ResultDto<Dictionary<string, int>>> GetReservationsByStatusAsync()
        {
            try
            {
                var statusCounts = await _unitOfWork.Reservations.Query()
                    .GroupBy(r => r.Status)
                    .Select(g => new { Status = g.Key, Count = g.Count() })
                    .ToDictionaryAsync(x => x.Status, x => x.Count);
                
                return ResultDto<Dictionary<string, int>>.Success(statusCounts);
            }
            catch (Exception ex)
            {
                return ResultDto<Dictionary<string, int>>.Failure("Failed to get reservations by status: " + ex.Message);
            }
        }

        public async Task<ResultDto<Dictionary<string, int>>> GetReservationsBySpecialtyAsync()
        {
            try
            {
                var specialtyCounts = await _unitOfWork.Reservations.Query()
                    .Include(r => r.DoctorSchedules)
                        .ThenInclude(ds => ds.Service)
                            .ThenInclude(s => s.Specialty)
                    .Where(r => r.DoctorSchedules.Any(ds => ds.Service.Specialty != null))
                    .GroupBy(r => r.DoctorSchedules.First().Service.Specialty.SpecialtyName)
                    .Select(g => new { Specialty = g.Key, Count = g.Count() })
                    .ToDictionaryAsync(x => x.Specialty, x => x.Count);
                
                return ResultDto<Dictionary<string, int>>.Success(specialtyCounts);
            }
            catch (Exception ex)
            {
                return ResultDto<Dictionary<string, int>>.Failure("Failed to get reservations by specialty: " + ex.Message);
            }
        }

        public async Task<ResultDto<Dictionary<string, decimal>>> GetRevenueBySpecialtyAsync(DateTime startDate, DateTime endDate)
        {
            try
            {
                // Ensure end date includes the full day
                endDate = endDate.Date.AddDays(1).AddTicks(-1);
                
                var specialtyRevenue = await _unitOfWork.Repository<Payment>().Query()
                    .Include(p => p.Reservation)
                        .ThenInclude(r => r.DoctorSchedules)
                            .ThenInclude(ds => ds.Service)
                                .ThenInclude(s => s.Specialty)
                    .Where(p => p.PaymentDate >= startDate && p.PaymentDate <= endDate && 
                           p.PaymentStatus == "Thành công" && 
                           p.Reservation.DoctorSchedules.Any(ds => ds.Service.Specialty != null))
                    .GroupBy(p => p.Reservation.DoctorSchedules.First().Service.Specialty.SpecialtyName)
                    .Select(g => new { Specialty = g.Key, Amount = g.Sum(p => p.Amount) })
                    .ToDictionaryAsync(x => x.Specialty, x => x.Amount);
                
                return ResultDto<Dictionary<string, decimal>>.Success(specialtyRevenue);
            }
            catch (Exception ex)
            {
                return ResultDto<Dictionary<string, decimal>>.Failure("Failed to get revenue by specialty: " + ex.Message);
            }
        }

        public async Task<ResultDto<List<RevenueByDateDto>>> GetRevenueByDateAsync(DateTime startDate, DateTime endDate, string groupBy = "day")
        {
            try
            {
                // Ensure end date includes the full day
                endDate = endDate.Date.AddDays(1).AddTicks(-1);
                
                var query = _unitOfWork.Repository<Payment>().Query()
                    .Where(p => p.PaymentDate >= startDate && p.PaymentDate <= endDate && 
                           p.PaymentStatus == "Thành công");
                
                // Group by different time periods
                List<RevenueByDateDto> result;
                
                switch (groupBy.ToLower())
                {
                    case "month":
                        result = await query
                            .GroupBy(p => new { Year = p.PaymentDate.Year, Month = p.PaymentDate.Month })
                            .Select(g => new RevenueByDateDto
                            {
                                Period = $"{g.Key.Year}-{g.Key.Month:D2}",
                                Revenue = g.Sum(p => p.Amount),
                                Count = g.Count()
                            })
                            .OrderBy(x => x.Period)
                            .ToListAsync();
                        break;
                    
                    case "week":
                        result = await query
                            .GroupBy(p => new { 
                                Year = p.PaymentDate.Year, 
                                Week = EF.Functions.DateDiffWeek(new DateTime(p.PaymentDate.Year, 1, 1), p.PaymentDate) + 1
                            })
                            .Select(g => new RevenueByDateDto
                            {
                                Period = $"{g.Key.Year}-W{g.Key.Week:D2}",
                                Revenue = g.Sum(p => p.Amount),
                                Count = g.Count()
                            })
                            .OrderBy(x => x.Period)
                            .ToListAsync();
                        break;
                    
                    case "day":
                    default:
                        result = await query
                            .GroupBy(p => p.PaymentDate.Date)
                            .Select(g => new RevenueByDateDto
                            {
                                Period = g.Key.ToString("yyyy-MM-dd"),
                                Revenue = g.Sum(p => p.Amount),
                                Count = g.Count()
                            })
                            .OrderBy(x => x.Period)
                            .ToListAsync();
                        break;
                }
                
                return ResultDto<List<RevenueByDateDto>>.Success(result);
            }
            catch (Exception ex)
            {
                return ResultDto<List<RevenueByDateDto>>.Failure("Failed to get revenue by date: " + ex.Message);
            }
        }

        public async Task<ResultDto<Dictionary<string, int>>> GetPatientsBySpecialtyAsync(DateTime startDate, DateTime endDate)
        {
            try
            {
                // Ensure end date includes the full day
                endDate = endDate.Date.AddDays(1).AddTicks(-1);
                
                var patientsBySpecialty = await _unitOfWork.Reservations.Query()
                    .Include(r => r.DoctorSchedules)
                        .ThenInclude(ds => ds.Service)
                            .ThenInclude(s => s.Specialty)
                    .Where(r => r.AppointmentDate >= startDate && r.AppointmentDate <= endDate && 
                           r.DoctorSchedules.Any(ds => ds.Service.Specialty != null))
                    .GroupBy(r => new { 
                        SpecialtyName = r.DoctorSchedules.First().Service.Specialty.SpecialtyName,
                        PatientId = r.PatientId
                    })
                    .GroupBy(g => g.Key.SpecialtyName)
                    .Select(g => new { Specialty = g.Key, Count = g.Count() })
                    .ToDictionaryAsync(x => x.Specialty, x => x.Count);
                
                return ResultDto<Dictionary<string, int>>.Success(patientsBySpecialty);
            }
            catch (Exception ex)
            {
                return ResultDto<Dictionary<string, int>>.Failure("Failed to get patients by specialty: " + ex.Message);
            }
        }

        public async Task<ResultDto<List<DoctorPerformanceDto>>> GetDoctorPerformanceAsync(DateTime startDate, DateTime endDate)
        {
            try
            {
                // Ensure end date includes the full day
                endDate = endDate.Date.AddDays(1).AddTicks(-1);
                
                var doctors = await _unitOfWork.Repository<Doctor>().Query()
                    .Include(d => d.DoctorNavigation)
                    .ToListAsync();
                
                var result = new List<DoctorPerformanceDto>();
                
                foreach (var doctor in doctors)
                {
                    // Get doctor's completed reservations
                    var completedReservations = await _unitOfWork.Reservations.Query()
                        .Include(r => r.DoctorSchedules)
                        .Where(r => r.AppointmentDate >= startDate && r.AppointmentDate <= endDate &&
                               r.Status == "Đã hoàn thành" &&
                               r.DoctorSchedules.Any(ds => ds.DoctorId == doctor.DoctorId))
                        .CountAsync();
                    
                    // Get doctor's revenue
                    var revenue = await _unitOfWork.Repository<Payment>().Query()
                        .Include(p => p.Reservation)
                            .ThenInclude(r => r.DoctorSchedules)
                        .Where(p => p.PaymentDate >= startDate && p.PaymentDate <= endDate &&
                               p.PaymentStatus == "Thành công" &&
                               p.Reservation.DoctorSchedules.Any(ds => ds.DoctorId == doctor.DoctorId))
                        .SumAsync(p => p.Amount);
                    
                    // Get doctor's patient count (unique patients)
                    var patientCount = await _unitOfWork.Reservations.Query()
                        .Include(r => r.DoctorSchedules)
                        .Where(r => r.AppointmentDate >= startDate && r.AppointmentDate <= endDate &&
                               r.DoctorSchedules.Any(ds => ds.DoctorId == doctor.DoctorId))
                        .Select(r => r.PatientId)
                        .Distinct()
                        .CountAsync();
                    
                    // Get doctor's average rating
                    var feedbacks = await _unitOfWork.Repository<Feedback>().Query()
                        .Include(f => f.Reservation)
                            .ThenInclude(r => r.DoctorSchedules)
                        .Where(f => f.Reservation.AppointmentDate >= startDate && 
                               f.Reservation.AppointmentDate <= endDate &&
                               f.Reservation.DoctorSchedules.Any(ds => ds.DoctorId == doctor.DoctorId))
                        .ToListAsync();
                    
                    var averageRating = feedbacks.Any() ? feedbacks.Average(f => f.Rating) : 0;
                    
                    result.Add(new DoctorPerformanceDto
                    {
                        DoctorId = doctor.DoctorId,
                        DoctorName = doctor.DoctorNavigation.UserName,
                        CompletedAppointments = completedReservations,
                        Revenue = revenue,
                        PatientCount = patientCount,
                        AverageRating = averageRating,
                        FeedbackCount = feedbacks.Count
                    });
                }
                
                // Sort by completed appointments (descending)
                result = result.OrderByDescending(d => d.CompletedAppointments).ToList();
                
                return ResultDto<List<DoctorPerformanceDto>>.Success(result);
            }
            catch (Exception ex)
            {
                return ResultDto<List<DoctorPerformanceDto>>.Failure("Failed to get doctor performance: " + ex.Message);
            }
        }

        public async Task<ResultDto<ReservationTrendsDto>> GetReservationTrendsAsync(int days)
        {
            try
            {
                var endDate = DateTime.Today;
                var startDate = endDate.AddDays(-days);
                
                // Get daily reservation counts
                var dailyReservations = await _unitOfWork.Reservations.Query()
                    .Where(r => r.AppointmentDate.Date >= startDate && r.AppointmentDate.Date <= endDate)
                    .GroupBy(r => r.AppointmentDate.Date)
                    .Select(g => new { Date = g.Key, Count = g.Count() })
                    .ToDictionaryAsync(x => x.Date.ToString("yyyy-MM-dd"), x => x.Count);
                
                // Fill in missing days with zero counts
                for (var date = startDate; date <= endDate; date = date.AddDays(1))
                {
                    var dateString = date.ToString("yyyy-MM-dd");
                    if (!dailyReservations.ContainsKey(dateString))
                    {
                        dailyReservations.Add(dateString, 0);
                    }
                }
                
                // Get growth rate
                var totalPreviousPeriod = await _unitOfWork.Reservations.Query()
                    .Where(r => r.AppointmentDate.Date >= startDate.AddDays(-days) && r.AppointmentDate.Date < startDate)
                    .CountAsync();
                
                var totalCurrentPeriod = await _unitOfWork.Reservations.Query()
                    .Where(r => r.AppointmentDate.Date >= startDate && r.AppointmentDate.Date <= endDate)
                    .CountAsync();
                
                var growthRate = totalPreviousPeriod > 0 
                    ? (double)(totalCurrentPeriod - totalPreviousPeriod) / totalPreviousPeriod * 100 
                    : 0;
                
                var trend = new ReservationTrendsDto
                {
                    DailyReservations = dailyReservations.OrderBy(x => x.Key).ToDictionary(x => x.Key, x => x.Value),
                    TotalReservations = totalCurrentPeriod,
                    GrowthRate = Math.Round(growthRate, 2)
                };
                
                return ResultDto<ReservationTrendsDto>.Success(trend);
            }
            catch (Exception ex)
            {
                return ResultDto<ReservationTrendsDto>.Failure("Failed to get reservation trends: " + ex.Message);
            }
        }

        public async Task<ResultDto<List<TopServiceDto>>> GetTopServicesAsync(DateTime startDate, DateTime endDate, int limit = 5)
        {
            try
            {
                // Ensure end date includes the full day
                endDate = endDate.Date.AddDays(1).AddTicks(-1);
                
                var topServices = await _unitOfWork.Reservations.Query()
                    .Include(r => r.DoctorSchedules)
                        .ThenInclude(ds => ds.Service)
                    .Where(r => r.AppointmentDate >= startDate && r.AppointmentDate <= endDate &&
                           r.Status != "Đã hủy")
                    .SelectMany(r => r.DoctorSchedules.Select(ds => ds.Service))
                    .GroupBy(s => new { s.ServiceId, s.ServiceName, s.Price })
                    .Select(g => new TopServiceDto
                    {
                        ServiceId = g.Key.ServiceId,
                        ServiceName = g.Key.ServiceName,
                        Count = g.Count(),
                        Revenue = g.Key.Price * g.Count()
                    })
                    .OrderByDescending(s => s.Count)
                    .Take(limit)
                    .ToListAsync();
                
                return ResultDto<List<TopServiceDto>>.Success(topServices);
            }
            catch (Exception ex)
            {
                return ResultDto<List<TopServiceDto>>.Failure("Failed to get top services: " + ex.Message);
            }
        }
    }
} 