using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Server;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class DoctorRepository
    {
        private readonly AppointmentSchedulingDbContext _context;
        public DoctorRepository(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }
        public async Task<IEnumerable<Doctor>> GetAllDoctorsAsync() => await _context.Doctors.ToListAsync();
        public async Task<Doctor> GetDoctorByIdAsync(int id) => await _context.Doctors.FindAsync(id);
    }
}
