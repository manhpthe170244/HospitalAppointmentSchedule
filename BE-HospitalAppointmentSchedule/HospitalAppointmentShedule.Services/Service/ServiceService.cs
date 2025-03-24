using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.IService;

namespace HospitalAppointmentShedule.Services.Services
{
    public class ServiceService : IServiceService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IMapper _mapper;

        public ServiceService(IUnitOfWork unitOfWork, IMapper mapper)
        {
            _unitOfWork = unitOfWork;
            _mapper = mapper;
        }

        public async Task<IEnumerable<ServiceDTO>> GetAllServicesAsync()
        {
            var services = await _unitOfWork.Services.GetAllAsync();
            return _mapper.Map<IEnumerable<ServiceDTO>>(services);
        }

        public async Task<ServiceDTO> GetServiceByIdAsync(int id)
        {
            var service = await _unitOfWork.Services.GetByIdAsync(id);
            return _mapper.Map<ServiceDTO>(service);
        }

        public async Task<ServiceDTO> CreateServiceAsync(ServiceDTO createServiceDto)
        {
            var service = _mapper.Map<Service>(createServiceDto);
            var createdService = await _unitOfWork.Services.AddAsync(service);
            await _unitOfWork.SaveChangesAsync();
            return _mapper.Map<ServiceDTO>(createdService);
        }

        public async Task<ServiceDTO> UpdateServiceAsync(int id, ServiceDTO updateServiceDto)
        {
            var existingService = await _unitOfWork.Services.GetByIdAsync(id);
            if (existingService == null)
                return null;

            var service = _mapper.Map<Service>(updateServiceDto);
            service.ServiceId = id;
            await _unitOfWork.Services.UpdateAsync(service);
            await _unitOfWork.SaveChangesAsync();
            return _mapper.Map<ServiceDTO>(service);
        }

        public async Task DeleteServiceAsync(int id)
        {
            await _unitOfWork.Services.DeleteAsync(id);
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task<IEnumerable<ServiceDTO>> GetServicesByCategoryAsync(int categoryId)
        {
            var services = await _unitOfWork.Services.GetServicesByCategoryAsync(categoryId);
            return _mapper.Map<IEnumerable<ServiceDTO>>(services);
        }

        public async Task<IEnumerable<ServiceDTO>> GetServicesBySpecialtyAsync(int specialtyId)
        {
            var services = await _unitOfWork.Services.GetServicesBySpecialtyAsync(specialtyId);
            return _mapper.Map<IEnumerable<ServiceDTO>>(services);
        }
    }
}