using HospitalAppointmentShedule.Services.DTOs;

namespace HospitalAppointmentShedule.Services.IService
{
    public interface IServiceService
    {
        Task<IEnumerable<ServiceDTO>> GetAllServicesAsync();
        Task<ServiceDTO> GetServiceByIdAsync(int id);
        Task<ServiceDTO> CreateServiceAsync(ServiceDTO createServiceDto);
        Task<ServiceDTO> UpdateServiceAsync(int id, ServiceDTO updateServiceDto);
        Task DeleteServiceAsync(int id);
        Task<IEnumerable<ServiceDTO>> GetServicesByCategoryAsync(int categoryId);
        Task<IEnumerable<ServiceDTO>> GetServicesBySpecialtyAsync(int specialtyId);
    }
} 