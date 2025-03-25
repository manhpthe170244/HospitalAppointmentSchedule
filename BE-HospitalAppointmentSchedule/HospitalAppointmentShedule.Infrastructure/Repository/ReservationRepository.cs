using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class ReservationRepository : GenericRepository<Reservation>, IReservationRepository
    {
        public ReservationRepository(AppointmentSchedulingDbContext context) : base(context)
        {
        }

        public async Task<Reservation?> GetReservationWithDetailsAsync(int reservationId)
        {
            return await _dbSet
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Room)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Include(r => r.Payments)
                .Include(r => r.MedicalRecords)
                .Include(r => r.Feedback)
                .FirstOrDefaultAsync(r => r.ReservationId == reservationId);
        }

        public async Task<IEnumerable<Reservation>> GetReservationsByDateRangeAsync(DateTime startDate, DateTime endDate)
        {
            return await _dbSet
                .Where(r => r.AppointmentDate >= startDate && r.AppointmentDate <= endDate)
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .ToListAsync();
        }

        public async Task<IEnumerable<Reservation>> GetReservationsByPatientAsync(int patientId)
        {
            return await _dbSet
                .Where(r => r.PatientId == patientId)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Include(r => r.Payments)
                .ToListAsync();
        }

        public async Task<IEnumerable<Reservation>> GetReservationsBySpecialtyAsync(int specialtyId, DateTime? date = null)
        {
            var query = _dbSet
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.Specialties)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                        .ThenInclude(s => s.Specialty)
                .Where(r => r.DoctorSchedules.Any(ds => 
                    ds.Doctor.Specialties.Any(s => s.SpecialtyId == specialtyId) || 
                    ds.Service.SpecialtyId == specialtyId));

            if (date.HasValue)
            {
                query = query.Where(r => r.AppointmentDate.Date == date.Value.Date);
            }

            return await query.ToListAsync();
        }

        public async Task<IEnumerable<Reservation>> GetReservationsByStatusAsync(string status)
        {
            return await _dbSet
                .Where(r => r.Status == status)
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Doctor)
                        .ThenInclude(d => d.DoctorNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .ToListAsync();
        }

        public async Task<bool> IsSlotAvailableAsync(int doctorScheduleId, DateTime appointmentDate)
        {
            var doctorSchedule = await _context.DoctorSchedules
                .FirstOrDefaultAsync(ds => ds.DoctorScheduleId == doctorScheduleId);

            if (doctorSchedule == null)
                return false;

            // Check if the day of week matches
            if (doctorSchedule.DayOfWeek != appointmentDate.DayOfWeek.ToString())
                return false;

            // Check if there's already a reservation for this slot
            var existingReservation = await _dbSet
                .Include(r => r.DoctorSchedules)
                .AnyAsync(r => 
                    r.DoctorSchedules.Any(ds => ds.DoctorScheduleId == doctorScheduleId) && 
                    r.AppointmentDate.Date == appointmentDate.Date &&
                    (r.Status == "Đã xác nhận" || r.Status == "Chờ thanh toán" || r.Status == "Đã thanh toán"));

            return !existingReservation;
        }
    }
} 