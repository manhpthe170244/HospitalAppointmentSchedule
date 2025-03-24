using System.Collections.Generic;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.DBContext;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class ServiceRepository : IServiceRepository
    {
        private readonly AppointmentSchedulingDbContext _context;

        public ServiceRepository(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Service>> GetAllAsync()
        {
            return await _context.Services
                .Include(s => s.ParentService)
                .Include(s => s.Specialty)
                .ToListAsync();
        }

        public async Task<Service> GetByIdAsync(int id)
        {
            return await _context.Services
                .Include(s => s.ParentService)
                .Include(s => s.Specialty)
                .FirstOrDefaultAsync(s => s.ServiceId == id);
        }

        public async Task<Service> AddAsync(Service service)
        {
            await _context.Services.AddAsync(service);
            await _context.SaveChangesAsync();
            return service;
        }

        public async Task UpdateAsync(Service service)
        {
            _context.Services.Update(service);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var service = await _context.Services.FindAsync(id);
            if (service != null)
            {
                _context.Services.Remove(service);
                await _context.SaveChangesAsync();
            }
        }

        public async Task<IEnumerable<Service>> GetServicesByCategoryAsync(int categoryId)
        {
            return await _context.Services
                .Include(s => s.ParentService)
                .Include(s => s.Specialty)
                .Where(s => s.ParentServiceId == categoryId)
                .ToListAsync();
        }

        public async Task<IEnumerable<Service>> GetServicesBySpecialtyAsync(int specialtyId)
        {
            return await _context.Services
                .Include(s => s.ParentService)
                .Include(s => s.Specialty)
                .Where(s => s.SpecialtyId == specialtyId)
                .ToListAsync();
        }
    }
}
