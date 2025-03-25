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
    public class DoctorRepository : GenericRepository<Doctor>, IDoctorRepository
    {
        public DoctorRepository(AppointmentSchedulingDbContext context) : base(context)
        {
        }

        public async Task<Doctor?> GetDoctorWithDetailsAsync(int doctorId)
        {
            return await _dbSet
                .Include(d => d.DoctorNavigation)
                .Include(d => d.Specialties)
                .Include(d => d.Services)
                .Include(d => d.Certifications)
                .Include(d => d.DoctorSchedules)
                    .ThenInclude(ds => ds.Room)
                .Include(d => d.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Include(d => d.DoctorSchedules)
                    .ThenInclude(ds => ds.Service)
                .FirstOrDefaultAsync(d => d.DoctorId == doctorId);
        }

        public async Task<IEnumerable<Doctor>> GetDoctorsByServiceAsync(int serviceId)
        {
            return await _dbSet
                .Include(d => d.DoctorNavigation)
                .Include(d => d.Services)
                .Where(d => d.Services.Any(s => s.ServiceId == serviceId))
                .ToListAsync();
        }

        public async Task<IEnumerable<Doctor>> GetDoctorsBySpecialtyAsync(int specialtyId)
        {
            return await _dbSet
                .Include(d => d.DoctorNavigation)
                .Include(d => d.Specialties)
                .Where(d => d.Specialties.Any(s => s.SpecialtyId == specialtyId))
                .ToListAsync();
        }

        public async Task<IEnumerable<DoctorSchedule>> GetDoctorSchedulesAsync(int doctorId, DateTime? date = null)
        {
            var query = _context.DoctorSchedules
                .Include(ds => ds.Room)
                .Include(ds => ds.Service)
                .Include(ds => ds.Slot)
                .Where(ds => ds.DoctorId == doctorId);

            if (date.HasValue)
            {
                string dayOfWeek = date.Value.DayOfWeek.ToString();
                query = query.Where(ds => ds.DayOfWeek == dayOfWeek);
            }

            return await query.ToListAsync();
        }

        public async Task<IEnumerable<Reservation>> GetDoctorReservationsAsync(int doctorId, DateTime? date = null)
        {
            var query = _context.Reservations
                .Include(r => r.Patient)
                    .ThenInclude(p => p.PatientNavigation)
                .Include(r => r.DoctorSchedules)
                    .ThenInclude(ds => ds.Slot)
                .Where(r => r.DoctorSchedules.Any(ds => ds.DoctorId == doctorId));

            if (date.HasValue)
            {
                query = query.Where(r => r.AppointmentDate.Date == date.Value.Date);
            }

            return await query.ToListAsync();
        }
    }
} 