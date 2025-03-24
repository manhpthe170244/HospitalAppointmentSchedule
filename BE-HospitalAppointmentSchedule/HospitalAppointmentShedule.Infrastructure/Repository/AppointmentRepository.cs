using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.DBContext;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class AppointmentRepository : IAppointmentRepository
    {
        private readonly AppointmentSchedulingDbContext _context;

        public AppointmentRepository(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Reservation>> GetUserAppointmentsAsync(int userId)
        {
            return await _context.Reservations
                .Include(r => r.Patient)
                .Include(r => r.DoctorSchedules)
                .Where(r => r.PatientId == userId)
                .ToListAsync();
        }

        public async Task<Reservation> GetAppointmentByIdAsync(int reservationId)
        {
            return await _context.Reservations
                .Include(r => r.Patient)
                .Include(r => r.DoctorSchedules)
                .Include(r => r.Feedback)
                .Include(r => r.MedicalRecords)
                .Include(r => r.Payments)
                .FirstOrDefaultAsync(r => r.ReservationId == reservationId);
        }

        public async Task AddAppointmentAsync(Reservation reservation)
        {
            reservation.UpdatedDate = DateTime.Now;
            await _context.Reservations.AddAsync(reservation);
            await _context.SaveChangesAsync();
        }

        public async Task CancelAppointmentAsync(int reservationId, string cancellationReason)
        {
            var reservation = await _context.Reservations.FindAsync(reservationId);
            if (reservation != null)
            {
                reservation.Status = "Cancelled";
                reservation.CancellationReason = cancellationReason;
                reservation.UpdatedDate = DateTime.Now;
                await _context.SaveChangesAsync();
            }
        }

        public async Task UpdateAppointmentAsync(Reservation reservation)
        {
            reservation.UpdatedDate = DateTime.Now;
            _context.Reservations.Update(reservation);
            await _context.SaveChangesAsync();
        }

        public async Task<IEnumerable<Reservation>> GetAppointmentsByStatusAsync(string status)
        {
            return await _context.Reservations
                .Include(r => r.Patient)
                .Include(r => r.DoctorSchedules)
                .Where(r => r.Status == status)
                .ToListAsync();
        }
    }
}
