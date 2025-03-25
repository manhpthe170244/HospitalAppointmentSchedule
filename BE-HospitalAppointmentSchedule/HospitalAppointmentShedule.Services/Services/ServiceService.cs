using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

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

        #region Service Methods
        public async Task<ResultDto<ServiceDto>> GetServiceByIdAsync(int serviceId)
        {
            var service = await _unitOfWork.Services.GetServiceWithDetailsAsync(serviceId);
            
            if (service == null)
                return ResultDto<ServiceDto>.Failure("Service not found");
            
            var serviceDto = _mapper.Map<ServiceDto>(service);
            return ResultDto<ServiceDto>.Success(serviceDto);
        }

        public async Task<ResultDto<ServiceDetailDto>> GetServiceDetailsAsync(int serviceId)
        {
            var service = await _unitOfWork.Services.GetServiceWithDetailsAsync(serviceId);
            
            if (service == null)
                return ResultDto<ServiceDetailDto>.Failure("Service not found");
            
            var serviceDetailDto = _mapper.Map<ServiceDetailDto>(service);
            return ResultDto<ServiceDetailDto>.Success(serviceDetailDto);
        }

        public async Task<ResultDto<List<ServiceDto>>> GetAllServicesAsync()
        {
            var services = await _unitOfWork.Services.Query()
                .Include(s => s.Specialty)
                .Include(s => s.ParentService)
                .ToListAsync();
            
            var serviceDtos = _mapper.Map<List<ServiceDto>>(services);
            return ResultDto<List<ServiceDto>>.Success(serviceDtos);
        }

        public async Task<ResultDto<PaginatedResultDto<ServiceDto>>> GetPaginatedServicesAsync(int pageIndex, int pageSize, string? searchTerm = null)
        {
            var query = _unitOfWork.Services.Query()
                .Include(s => s.Specialty)
                .Include(s => s.ParentService)
                .AsQueryable();
            
            if (!string.IsNullOrEmpty(searchTerm))
            {
                query = query.Where(s => 
                    s.ServiceName.Contains(searchTerm) || 
                    (s.Overview != null && s.Overview.Contains(searchTerm)) ||
                    s.Specialty.SpecialtyName.Contains(searchTerm));
            }
            
            var totalCount = await query.CountAsync();
            var services = await query
                .OrderBy(s => s.ServiceId)
                .Skip((pageIndex - 1) * pageSize)
                .Take(pageSize)
                .ToListAsync();
            
            var serviceDtos = _mapper.Map<List<ServiceDto>>(services);
            
            var paginatedResult = PaginatedResultDto<ServiceDto>.Create(
                serviceDtos,
                totalCount,
                pageIndex,
                pageSize);
            
            return ResultDto<PaginatedResultDto<ServiceDto>>.Success(paginatedResult);
        }

        public async Task<ResultDto<List<ServiceDto>>> GetServicesBySpecialtyAsync(int specialtyId)
        {
            var services = await _unitOfWork.Services.GetServicesBySpecialtyAsync(specialtyId);
            var serviceDtos = _mapper.Map<List<ServiceDto>>(services);
            return ResultDto<List<ServiceDto>>.Success(serviceDtos);
        }

        public async Task<ResultDto<List<ServiceDto>>> GetChildServicesAsync(int parentServiceId)
        {
            var childServices = await _unitOfWork.Services.GetChildServicesAsync(parentServiceId);
            var childServiceDtos = _mapper.Map<List<ServiceDto>>(childServices);
            return ResultDto<List<ServiceDto>>.Success(childServiceDtos);
        }

        public async Task<ResultDto<ServiceDto>> CreateServiceAsync(ServiceCreateDto serviceDto)
        {
            // Validate specialty exists
            var specialty = await _unitOfWork.Repository<Specialty>().GetByIdAsync(serviceDto.SpecialtyId);
            if (specialty == null)
                return ResultDto<ServiceDto>.Failure("Specialty not found");
            
            // Validate parent service if provided
            if (serviceDto.ParentServiceId.HasValue)
            {
                var parentService = await _unitOfWork.Services.GetByIdAsync(serviceDto.ParentServiceId.Value);
                if (parentService == null)
                    return ResultDto<ServiceDto>.Failure("Parent service not found");
            }
            
            // Check if service name already exists within the specialty
            var serviceNameExists = await _unitOfWork.Services.Query()
                .AnyAsync(s => s.ServiceName == serviceDto.ServiceName && s.SpecialtyId == serviceDto.SpecialtyId);
            
            if (serviceNameExists)
                return ResultDto<ServiceDto>.Failure("A service with this name already exists in the specialty");
            
            var service = _mapper.Map<Service>(serviceDto);
            
            // Add devices if any
            if (serviceDto.DeviceIds != null && serviceDto.DeviceIds.Any())
            {
                var devices = await _unitOfWork.Repository<Device>().Query()
                    .Where(d => serviceDto.DeviceIds.Contains(d.DeviceId))
                    .ToListAsync();
                
                service.Devices = devices;
            }
            
            await _unitOfWork.Services.AddAsync(service);
            await _unitOfWork.SaveChangesAsync();
            
            var createdService = await _unitOfWork.Services.GetServiceWithDetailsAsync(service.ServiceId);
            var serviceResponseDto = _mapper.Map<ServiceDto>(createdService);
            
            return ResultDto<ServiceDto>.Success(serviceResponseDto, "Service created successfully");
        }

        public async Task<ResultDto<ServiceDto>> UpdateServiceAsync(int serviceId, ServiceUpdateDto serviceDto)
        {
            var service = await _unitOfWork.Services.GetServiceWithDetailsAsync(serviceId);
            
            if (service == null)
                return ResultDto<ServiceDto>.Failure("Service not found");
            
            // Validate specialty if provided
            if (serviceDto.SpecialtyId.HasValue)
            {
                var specialty = await _unitOfWork.Repository<Specialty>().GetByIdAsync(serviceDto.SpecialtyId.Value);
                if (specialty == null)
                    return ResultDto<ServiceDto>.Failure("Specialty not found");
            }
            
            // Validate parent service if provided
            if (serviceDto.ParentServiceId.HasValue)
            {
                // Ensure we're not creating a circular reference
                if (serviceDto.ParentServiceId.Value == serviceId)
                    return ResultDto<ServiceDto>.Failure("A service cannot be its own parent");
                
                var parentService = await _unitOfWork.Services.GetByIdAsync(serviceDto.ParentServiceId.Value);
                if (parentService == null)
                    return ResultDto<ServiceDto>.Failure("Parent service not found");
            }
            
            // Check if service name already exists within the specialty (if name is being changed)
            if (serviceDto.ServiceName != null && service.ServiceName != serviceDto.ServiceName)
            {
                var specialtyId = serviceDto.SpecialtyId ?? service.SpecialtyId;
                var serviceNameExists = await _unitOfWork.Services.Query()
                    .AnyAsync(s => s.ServiceName == serviceDto.ServiceName && 
                                  s.SpecialtyId == specialtyId && 
                                  s.ServiceId != serviceId);
                
                if (serviceNameExists)
                    return ResultDto<ServiceDto>.Failure("A service with this name already exists in the specialty");
            }
            
            // Update properties
            _mapper.Map(serviceDto, service);
            
            // Update devices if provided
            if (serviceDto.DeviceIds != null)
            {
                var devices = await _unitOfWork.Repository<Device>().Query()
                    .Where(d => serviceDto.DeviceIds.Contains(d.DeviceId))
                    .ToListAsync();
                
                service.Devices.Clear();
                foreach (var device in devices)
                {
                    service.Devices.Add(device);
                }
            }
            
            _unitOfWork.Services.Update(service);
            await _unitOfWork.SaveChangesAsync();
            
            var updatedService = await _unitOfWork.Services.GetServiceWithDetailsAsync(serviceId);
            var serviceResponseDto = _mapper.Map<ServiceDto>(updatedService);
            
            return ResultDto<ServiceDto>.Success(serviceResponseDto, "Service updated successfully");
        }

        public async Task<ResultDto<bool>> DeleteServiceAsync(int serviceId)
        {
            var service = await _unitOfWork.Services.GetByIdAsync(serviceId);
            
            if (service == null)
                return ResultDto<bool>.Failure("Service not found");
            
            // Check if service has child services
            var hasChildServices = await _unitOfWork.Services.Query()
                .AnyAsync(s => s.ParentServiceId == serviceId);
            
            if (hasChildServices)
                return ResultDto<bool>.Failure("Cannot delete service with child services");
            
            // Check if service is assigned to any doctor schedules
            var usedInSchedules = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .AnyAsync(ds => ds.ServiceId == serviceId);
            
            if (usedInSchedules)
                return ResultDto<bool>.Failure("Cannot delete service that is used in doctor schedules");
            
            _unitOfWork.Services.Delete(service);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Service deleted successfully");
        }
        #endregion

        #region Specialty Methods
        public async Task<ResultDto<SpecialtyDto>> GetSpecialtyByIdAsync(int specialtyId)
        {
            var specialty = await _unitOfWork.Repository<Specialty>().GetByIdAsync(specialtyId);
            
            if (specialty == null)
                return ResultDto<SpecialtyDto>.Failure("Specialty not found");
            
            var specialtyDto = _mapper.Map<SpecialtyDto>(specialty);
            return ResultDto<SpecialtyDto>.Success(specialtyDto);
        }

        public async Task<ResultDto<List<SpecialtyDto>>> GetAllSpecialtiesAsync()
        {
            var specialties = await _unitOfWork.Repository<Specialty>().GetAllAsync();
            var specialtyDtos = _mapper.Map<List<SpecialtyDto>>(specialties);
            return ResultDto<List<SpecialtyDto>>.Success(specialtyDtos);
        }

        public async Task<ResultDto<SpecialtyDto>> CreateSpecialtyAsync(SpecialtyCreateDto specialtyDto)
        {
            // Check if specialty name already exists
            var specialtyNameExists = await _unitOfWork.Repository<Specialty>().Query()
                .AnyAsync(s => s.SpecialtyName == specialtyDto.SpecialtyName);
            
            if (specialtyNameExists)
                return ResultDto<SpecialtyDto>.Failure("A specialty with this name already exists");
            
            var specialty = _mapper.Map<Specialty>(specialtyDto);
            
            await _unitOfWork.Repository<Specialty>().AddAsync(specialty);
            await _unitOfWork.SaveChangesAsync();
            
            var specialtyResponseDto = _mapper.Map<SpecialtyDto>(specialty);
            return ResultDto<SpecialtyDto>.Success(specialtyResponseDto, "Specialty created successfully");
        }

        public async Task<ResultDto<SpecialtyDto>> UpdateSpecialtyAsync(int specialtyId, SpecialtyUpdateDto specialtyDto)
        {
            var specialty = await _unitOfWork.Repository<Specialty>().GetByIdAsync(specialtyId);
            
            if (specialty == null)
                return ResultDto<SpecialtyDto>.Failure("Specialty not found");
            
            // Check if specialty name already exists (if name is being changed)
            if (specialtyDto.SpecialtyName != null && specialty.SpecialtyName != specialtyDto.SpecialtyName)
            {
                var specialtyNameExists = await _unitOfWork.Repository<Specialty>().Query()
                    .AnyAsync(s => s.SpecialtyName == specialtyDto.SpecialtyName && s.SpecialtyId != specialtyId);
                
                if (specialtyNameExists)
                    return ResultDto<SpecialtyDto>.Failure("A specialty with this name already exists");
            }
            
            _mapper.Map(specialtyDto, specialty);
            
            _unitOfWork.Repository<Specialty>().Update(specialty);
            await _unitOfWork.SaveChangesAsync();
            
            var specialtyResponseDto = _mapper.Map<SpecialtyDto>(specialty);
            return ResultDto<SpecialtyDto>.Success(specialtyResponseDto, "Specialty updated successfully");
        }

        public async Task<ResultDto<bool>> DeleteSpecialtyAsync(int specialtyId)
        {
            var specialty = await _unitOfWork.Repository<Specialty>().GetByIdAsync(specialtyId);
            
            if (specialty == null)
                return ResultDto<bool>.Failure("Specialty not found");
            
            // Check if specialty has services
            var hasServices = await _unitOfWork.Services.Query()
                .AnyAsync(s => s.SpecialtyId == specialtyId);
            
            if (hasServices)
                return ResultDto<bool>.Failure("Cannot delete specialty with associated services");
            
            // Check if specialty is assigned to any doctors
            var usedByDoctors = await _unitOfWork.Repository<Doctor>().Query()
                .Include(d => d.Specialties)
                .AnyAsync(d => d.Specialties.Any(s => s.SpecialtyId == specialtyId));
            
            if (usedByDoctors)
                return ResultDto<bool>.Failure("Cannot delete specialty that is assigned to doctors");
            
            _unitOfWork.Repository<Specialty>().Delete(specialty);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Specialty deleted successfully");
        }
        #endregion

        #region Device Methods
        public async Task<ResultDto<List<DeviceDto>>> GetAllDevicesAsync()
        {
            var devices = await _unitOfWork.Repository<Device>().GetAllAsync();
            var deviceDtos = _mapper.Map<List<DeviceDto>>(devices);
            return ResultDto<List<DeviceDto>>.Success(deviceDtos);
        }
        #endregion
    }
} 