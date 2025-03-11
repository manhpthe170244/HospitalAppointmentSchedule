using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Server;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class AppointmentRepository
    {
        private readonly AppointmentSchedulingDbContext _context;
        public AppointmentRepository(AppointmentSchedulingDbContext context) { _context = context; }
        public async Task<IEnumerable<Reservation>> GetUserAppointmentsAsync(int userId)
        {
           return await _context.Reservations.Where(r => r.PatientId == userId).ToListAsync();
        }
           
        public async Task AddAppointmentAsync(Reservation reservation)
        {
            await _context.Reservations.AddAsync(reservation); await _context.SaveChangesAsync();
        }
        public async Task CancelAppointmentAsync(int reservationId)
        {
            var reservation = await _context.Reservations.FindAsync(reservationId);
            if (reservation != null) { _context.Reservations.Remove(reservation); await _context.SaveChangesAsync(); }
        }
        public async Task UpdateAppointmentAsync(Reservation reservation) 
        { 
            _context.Reservations.Update(reservation); await _context.SaveChangesAsync(); 
        }
    }
}
