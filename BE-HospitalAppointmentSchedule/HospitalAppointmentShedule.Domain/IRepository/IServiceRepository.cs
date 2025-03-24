using HospitalAppointmentShedule.Domain.Models;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IServiceRepository
    {
        Task<IEnumerable<Service>> GetAllAsync();
        Task<Service> GetByIdAsync(int id);
        Task<Service> AddAsync(Service service);
        Task UpdateAsync(Service service);
        Task DeleteAsync(int id);
        Task<IEnumerable<Service>> GetServicesByCategoryAsync(int categoryId);
        Task<IEnumerable<Service>> GetServicesBySpecialtyAsync(int specialtyId);
    }
}
