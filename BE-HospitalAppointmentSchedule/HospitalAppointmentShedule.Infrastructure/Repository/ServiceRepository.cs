using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class ServiceRepository : GenericRepository<Service>, IServiceRepository
    {
        public ServiceRepository(AppointmentSchedulingDbContext context) : base(context)
        {
        }

        public async Task<IEnumerable<Service>> GetChildServicesAsync(int parentServiceId)
        {
            return await _dbSet
                .Where(s => s.ParentServiceId == parentServiceId)
                .ToListAsync();
        }

        public async Task<Service?> GetServiceWithDetailsAsync(int serviceId)
        {
            return await _dbSet
                .Include(s => s.Specialty)
                .Include(s => s.Devices)
                .Include(s => s.Doctors)
                .Include(s => s.InverseParentService)
                .FirstOrDefaultAsync(s => s.ServiceId == serviceId);
        }

        public async Task<IEnumerable<Service>> GetServicesBySpecialtyAsync(int specialtyId)
        {
            return await _dbSet
                .Where(s => s.SpecialtyId == specialtyId)
                .ToListAsync();
        }

        public async Task<IEnumerable<Service>> GetServicesWithDevicesAsync()
        {
            return await _dbSet
                .Include(s => s.Devices)
                .ToListAsync();
        }
    }
} 