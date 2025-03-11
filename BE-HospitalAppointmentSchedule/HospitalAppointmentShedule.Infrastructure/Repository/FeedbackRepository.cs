using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Server;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class FeedbackRepository : IFeedbackRepository
    {
        private readonly AppointmentSchedulingDbContext _context;
        public FeedbackRepository(AppointmentSchedulingDbContext context) { _context = context; }
        public async Task SubmitFeedbackAsync(Feedback feedback) { await _context.Feedbacks.AddAsync(feedback); await _context.SaveChangesAsync(); }
        public async Task<IEnumerable<Feedback>> GetDoctorFeedbackAsync(int doctorscheduleId) =>
            await _context.Feedbacks.Where(f => f.Reservation.DoctorScheduleId == doctorscheduleId).ToListAsync();
    }
}
