using HospitalAppointmentShedule.Services.DTOs;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IDoctorService
    {
        Task<ResultDto<DoctorDto>> GetDoctorByIdAsync(int doctorId);
        Task<ResultDto<DoctorDetailDto>> GetDoctorDetailsAsync(int doctorId);
        Task<ResultDto<List<DoctorDto>>> GetAllDoctorsAsync();
        Task<ResultDto<PaginatedResultDto<DoctorDto>>> GetPaginatedDoctorsAsync(int pageIndex, int pageSize, string? searchTerm = null);
        Task<ResultDto<List<DoctorDto>>> GetDoctorsBySpecialtyAsync(int specialtyId);
        Task<ResultDto<List<DoctorDto>>> GetDoctorsByServiceAsync(int serviceId);
        Task<ResultDto<DoctorDto>> CreateDoctorAsync(DoctorCreateDto doctorDto);
        Task<ResultDto<DoctorDto>> UpdateDoctorAsync(int doctorId, DoctorUpdateDto doctorDto);
        Task<ResultDto<bool>> DeleteDoctorAsync(int doctorId);
        Task<ResultDto<List<DoctorScheduleDto>>> GetDoctorSchedulesAsync(int doctorId, DateTime? date = null);
        Task<ResultDto<DoctorScheduleDto>> CreateDoctorScheduleAsync(DoctorScheduleCreateDto scheduleDto);
        Task<ResultDto<bool>> DeleteDoctorScheduleAsync(int scheduleId);
        Task<ResultDto<CertificationDto>> AddCertificationAsync(int doctorId, string url, string? description);
        Task<ResultDto<bool>> DeleteCertificationAsync(int certificationId);
    }
} 