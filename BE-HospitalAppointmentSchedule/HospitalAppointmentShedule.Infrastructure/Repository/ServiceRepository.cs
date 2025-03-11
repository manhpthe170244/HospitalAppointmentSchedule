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
    public class ServiceRepository : IServiceRepository
    {
        private readonly AppointmentSchedulingDbContext _context;
        public ServiceRepository(AppointmentSchedulingDbContext context) { _context = context; }
        public async Task<IEnumerable<Service>> GetAllServicesAsync() => await _context.Services.ToListAsync();
        public async Task<Service> GetServiceByIdAsync(int id) => await _context.Services.FindAsync(id);
    }
}
