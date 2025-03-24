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
    public class FeedbackRepository : IFeedbackRepository
    {
        private readonly AppointmentSchedulingDbContext _context;

        public FeedbackRepository(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Feedback>> GetAllAsync()
        {
            return await _context.Feedbacks
                .Include(f => f.Reservation)
                .Include(f => f.Reservation.Patient)
                .Include(f => f.Reservation.DoctorSchedules)
                .ToListAsync();
        }

        public async Task<Feedback> GetByIdAsync(int id)
        {
            return await _context.Feedbacks
                .Include(f => f.Reservation)
                .Include(f => f.Reservation.Patient)
                .Include(f => f.Reservation.DoctorSchedules)
                .FirstOrDefaultAsync(f => f.FeedbackId == id);
        }

        public async Task<Feedback> AddAsync(Feedback feedback)
        {
            await _context.Feedbacks.AddAsync(feedback);
            await _context.SaveChangesAsync();
            return feedback;
        }

        public async Task UpdateAsync(Feedback feedback)
        {
            _context.Feedbacks.Update(feedback);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var feedback = await _context.Feedbacks.FindAsync(id);
            if (feedback != null)
            {
                _context.Feedbacks.Remove(feedback);
                await _context.SaveChangesAsync();
            }
        }

        public async Task<IEnumerable<Feedback>> GetFeedbacksByReservationIdAsync(int reservationId)
        {
            return await _context.Feedbacks
                .Include(f => f.Reservation)
                .Include(f => f.Reservation.Patient)
                .Include(f => f.Reservation.DoctorSchedules)
                .Where(f => f.ReservationId == reservationId)
                .ToListAsync();
        }

        public async Task<IEnumerable<Feedback>> GetFeedbacksByDoctorIdAsync(int doctorId)
        {
            return await _context.Feedbacks
                .Include(f => f.Reservation)
                .Include(f => f.Reservation.Patient)
                .Include(f => f.Reservation.DoctorSchedules)
                .Where(f => f.Reservation.DoctorSchedules.Any(ds => ds.DoctorId == doctorId))
                .ToListAsync();
        }
    }
}
