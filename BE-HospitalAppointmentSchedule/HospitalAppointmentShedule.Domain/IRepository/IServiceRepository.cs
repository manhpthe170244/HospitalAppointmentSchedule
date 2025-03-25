using HospitalAppointmentShedule.Domain.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IServiceRepository : IGenericRepository<Service>
    {
        Task<Service?> GetServiceWithDetailsAsync(int serviceId);
        Task<IEnumerable<Service>> GetServicesBySpecialtyAsync(int specialtyId);
        Task<IEnumerable<Service>> GetServicesWithDevicesAsync();
        Task<IEnumerable<Service>> GetChildServicesAsync(int parentServiceId);
    }
} 