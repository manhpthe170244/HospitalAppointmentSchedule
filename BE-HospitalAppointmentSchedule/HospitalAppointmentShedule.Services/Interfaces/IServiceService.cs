using HospitalAppointmentShedule.Services.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IServiceService
    {
        Task<ResultDto<ServiceDto>> GetServiceByIdAsync(int serviceId);
        Task<ResultDto<ServiceDetailDto>> GetServiceDetailsAsync(int serviceId);
        Task<ResultDto<List<ServiceDto>>> GetAllServicesAsync();
        Task<ResultDto<PaginatedResultDto<ServiceDto>>> GetPaginatedServicesAsync(int pageIndex, int pageSize, string? searchTerm = null);
        Task<ResultDto<List<ServiceDto>>> GetServicesBySpecialtyAsync(int specialtyId);
        Task<ResultDto<List<ServiceDto>>> GetChildServicesAsync(int parentServiceId);
        Task<ResultDto<ServiceDto>> CreateServiceAsync(ServiceCreateDto serviceDto);
        Task<ResultDto<ServiceDto>> UpdateServiceAsync(int serviceId, ServiceUpdateDto serviceDto);
        Task<ResultDto<bool>> DeleteServiceAsync(int serviceId);
        Task<ResultDto<SpecialtyDto>> GetSpecialtyByIdAsync(int specialtyId);
        Task<ResultDto<List<SpecialtyDto>>> GetAllSpecialtiesAsync();
        Task<ResultDto<SpecialtyDto>> CreateSpecialtyAsync(SpecialtyCreateDto specialtyDto);
        Task<ResultDto<SpecialtyDto>> UpdateSpecialtyAsync(int specialtyId, SpecialtyUpdateDto specialtyDto);
        Task<ResultDto<bool>> DeleteSpecialtyAsync(int specialtyId);
        Task<ResultDto<List<DeviceDto>>> GetAllDevicesAsync();
    }
} 