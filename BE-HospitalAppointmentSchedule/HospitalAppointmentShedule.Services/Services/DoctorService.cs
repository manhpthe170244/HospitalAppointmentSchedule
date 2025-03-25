using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Services
{
    public class DoctorService : IDoctorService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IMapper _mapper;
        private readonly IUserService _userService;

        public DoctorService(IUnitOfWork unitOfWork, IMapper mapper, IUserService userService)
        {
            _unitOfWork = unitOfWork;
            _mapper = mapper;
            _userService = userService;
        }

        public async Task<ResultDto<DoctorDto>> GetDoctorByIdAsync(int doctorId)
        {
            var doctor = await _unitOfWork.Doctors.GetDoctorWithDetailsAsync(doctorId);
            
            if (doctor == null)
                return ResultDto<DoctorDto>.Failure("Doctor not found");
            
            var doctorDto = _mapper.Map<DoctorDto>(doctor);
            return ResultDto<DoctorDto>.Success(doctorDto);
        }

        public async Task<ResultDto<DoctorDetailDto>> GetDoctorDetailsAsync(int doctorId)
        {
            var doctor = await _unitOfWork.Doctors.GetDoctorWithDetailsAsync(doctorId);
            
            if (doctor == null)
                return ResultDto<DoctorDetailDto>.Failure("Doctor not found");
            
            var doctorDetailDto = _mapper.Map<DoctorDetailDto>(doctor);
            return ResultDto<DoctorDetailDto>.Success(doctorDetailDto);
        }

        public async Task<ResultDto<List<DoctorDto>>> GetAllDoctorsAsync()
        {
            var doctors = await _unitOfWork.Doctors.Query()
                .Include(d => d.DoctorNavigation)
                .Include(d => d.Specialties)
                .Include(d => d.Certifications)
                .ToListAsync();
            
            var doctorDtos = _mapper.Map<List<DoctorDto>>(doctors);
            return ResultDto<List<DoctorDto>>.Success(doctorDtos);
        }

        public async Task<ResultDto<PaginatedResultDto<DoctorDto>>> GetPaginatedDoctorsAsync(int pageIndex, int pageSize, string? searchTerm = null)
        {
            var query = _unitOfWork.Doctors.Query()
                .Include(d => d.DoctorNavigation)
                .Include(d => d.Specialties)
                .AsQueryable();
            
            if (!string.IsNullOrEmpty(searchTerm))
            {
                query = query.Where(d => 
                    d.DoctorNavigation.UserName.Contains(searchTerm) || 
                    d.DoctorNavigation.Email.Contains(searchTerm) || 
                    d.DoctorNavigation.Phone.Contains(searchTerm) ||
                    d.Degree != null && d.Degree.Contains(searchTerm) ||
                    d.AcademicTitle != null && d.AcademicTitle.Contains(searchTerm) ||
                    d.Specialties.Any(s => s.SpecialtyName.Contains(searchTerm)));
            }
            
            var totalCount = await query.CountAsync();
            var doctors = await query
                .OrderBy(d => d.DoctorId)
                .Skip((pageIndex - 1) * pageSize)
                .Take(pageSize)
                .ToListAsync();
            
            var doctorDtos = _mapper.Map<List<DoctorDto>>(doctors);
            
            var paginatedResult = PaginatedResultDto<DoctorDto>.Create(
                doctorDtos,
                totalCount,
                pageIndex,
                pageSize);
            
            return ResultDto<PaginatedResultDto<DoctorDto>>.Success(paginatedResult);
        }

        public async Task<ResultDto<List<DoctorDto>>> GetDoctorsBySpecialtyAsync(int specialtyId)
        {
            var doctors = await _unitOfWork.Doctors.GetDoctorsBySpecialtyAsync(specialtyId);
            var doctorDtos = _mapper.Map<List<DoctorDto>>(doctors);
            return ResultDto<List<DoctorDto>>.Success(doctorDtos);
        }

        public async Task<ResultDto<List<DoctorDto>>> GetDoctorsByServiceAsync(int serviceId)
        {
            var doctors = await _unitOfWork.Doctors.GetDoctorsByServiceAsync(serviceId);
            var doctorDtos = _mapper.Map<List<DoctorDto>>(doctors);
            return ResultDto<List<DoctorDto>>.Success(doctorDtos);
        }

        public async Task<ResultDto<DoctorDto>> CreateDoctorAsync(DoctorCreateDto doctorDto)
        {
            // First create user account
            var createUserResult = await _userService.CreateUserAsync(doctorDto.User);
            
            if (!createUserResult.IsSuccess)
                return ResultDto<DoctorDto>.Failure(createUserResult.Message, createUserResult.Errors);
            
            var user = await _unitOfWork.Users.GetByIdAsync(createUserResult.Data.UserId);
            
            // Assign doctor role if not already assigned
            var doctorRole = await _unitOfWork.Repository<Role>().Query()
                .FirstOrDefaultAsync(r => r.RoleName == "Doctor");
            
            if (doctorRole != null && !user.Roles.Any(r => r.RoleId == doctorRole.RoleId))
            {
                user.Roles.Add(doctorRole);
                await _unitOfWork.SaveChangesAsync();
            }
            
            // Create doctor profile
            var doctor = new Doctor
            {
                DoctorId = user.UserId,
                Degree = doctorDto.Degree,
                AcademicTitle = doctorDto.AcademicTitle,
                Experience = doctorDto.Experience,
                Biography = doctorDto.Biography
            };
            
            // Assign specialties
            if (doctorDto.SpecialtyIds.Any())
            {
                var specialties = await _unitOfWork.Repository<Specialty>().Query()
                    .Where(s => doctorDto.SpecialtyIds.Contains(s.SpecialtyId))
                    .ToListAsync();
                
                doctor.Specialties = specialties;
            }
            
            // Assign services
            if (doctorDto.ServiceIds.Any())
            {
                var services = await _unitOfWork.Repository<Service>().Query()
                    .Where(s => doctorDto.ServiceIds.Contains(s.ServiceId))
                    .ToListAsync();
                
                doctor.Services = services;
            }
            
            await _unitOfWork.Doctors.AddAsync(doctor);
            await _unitOfWork.SaveChangesAsync();
            
            var createdDoctor = await _unitOfWork.Doctors.GetDoctorWithDetailsAsync(doctor.DoctorId);
            var doctorResponseDto = _mapper.Map<DoctorDto>(createdDoctor);
            
            return ResultDto<DoctorDto>.Success(doctorResponseDto, "Doctor created successfully");
        }

        public async Task<ResultDto<DoctorDto>> UpdateDoctorAsync(int doctorId, DoctorUpdateDto doctorDto)
        {
            var doctor = await _unitOfWork.Doctors.GetDoctorWithDetailsAsync(doctorId);
            
            if (doctor == null)
                return ResultDto<DoctorDto>.Failure("Doctor not found");
            
            // Update user information if provided
            if (doctorDto.User != null)
            {
                var updateUserResult = await _userService.UpdateUserAsync(doctorId, doctorDto.User);
                
                if (!updateUserResult.IsSuccess)
                    return ResultDto<DoctorDto>.Failure(updateUserResult.Message, updateUserResult.Errors);
            }
            
            // Update doctor properties
            if (doctorDto.Degree != null)
                doctor.Degree = doctorDto.Degree;
                
            if (doctorDto.AcademicTitle != null)
                doctor.AcademicTitle = doctorDto.AcademicTitle;
                
            if (doctorDto.Experience.HasValue)
                doctor.Experience = doctorDto.Experience;
                
            if (doctorDto.Biography != null)
                doctor.Biography = doctorDto.Biography;
            
            // Update specialties if provided
            if (doctorDto.SpecialtyIds != null && doctorDto.SpecialtyIds.Any())
            {
                var specialties = await _unitOfWork.Repository<Specialty>().Query()
                    .Where(s => doctorDto.SpecialtyIds.Contains(s.SpecialtyId))
                    .ToListAsync();
                
                doctor.Specialties.Clear();
                foreach (var specialty in specialties)
                {
                    doctor.Specialties.Add(specialty);
                }
            }
            
            // Update services if provided
            if (doctorDto.ServiceIds != null && doctorDto.ServiceIds.Any())
            {
                var services = await _unitOfWork.Repository<Service>().Query()
                    .Where(s => doctorDto.ServiceIds.Contains(s.ServiceId))
                    .ToListAsync();
                
                doctor.Services.Clear();
                foreach (var service in services)
                {
                    doctor.Services.Add(service);
                }
            }
            
            _unitOfWork.Doctors.Update(doctor);
            await _unitOfWork.SaveChangesAsync();
            
            var updatedDoctor = await _unitOfWork.Doctors.GetDoctorWithDetailsAsync(doctorId);
            var doctorResponseDto = _mapper.Map<DoctorDto>(updatedDoctor);
            
            return ResultDto<DoctorDto>.Success(doctorResponseDto, "Doctor updated successfully");
        }

        public async Task<ResultDto<bool>> DeleteDoctorAsync(int doctorId)
        {
            var doctor = await _unitOfWork.Doctors.GetByIdAsync(doctorId);
            
            if (doctor == null)
                return ResultDto<bool>.Failure("Doctor not found");
            
            // Check if doctor has active schedules or reservations
            var hasActiveSchedules = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .AnyAsync(ds => ds.DoctorId == doctorId);
            
            if (hasActiveSchedules)
                return ResultDto<bool>.Failure("Cannot delete doctor with active schedules");
            
            _unitOfWork.Doctors.Delete(doctor);
            
            // Delete the user account
            var userDeleteResult = await _userService.DeleteUserAsync(doctorId);
            
            if (!userDeleteResult.IsSuccess)
                return ResultDto<bool>.Failure(userDeleteResult.Message);
            
            return ResultDto<bool>.Success(true, "Doctor deleted successfully");
        }

        public async Task<ResultDto<List<DoctorScheduleDto>>> GetDoctorSchedulesAsync(int doctorId, DateTime? date = null)
        {
            var schedules = await _unitOfWork.Doctors.GetDoctorSchedulesAsync(doctorId, date);
            
            var scheduleDtos = _mapper.Map<List<DoctorScheduleDto>>(schedules);
            
            // Check availability for each schedule
            foreach (var scheduleDto in scheduleDtos)
            {
                if (date.HasValue)
                {
                    var isAvailable = await _unitOfWork.Reservations.IsSlotAvailableAsync(
                        scheduleDto.DoctorScheduleId, 
                        date.Value);
                    
                    scheduleDto.IsAvailable = isAvailable;
                }
            }
            
            return ResultDto<List<DoctorScheduleDto>>.Success(scheduleDtos);
        }

        public async Task<ResultDto<DoctorScheduleDto>> CreateDoctorScheduleAsync(DoctorScheduleCreateDto scheduleDto)
        {
            // Validate doctor exists
            var doctor = await _unitOfWork.Doctors.GetByIdAsync(scheduleDto.DoctorId);
            if (doctor == null)
                return ResultDto<DoctorScheduleDto>.Failure("Doctor not found");
            
            // Validate service exists
            var service = await _unitOfWork.Repository<Service>().GetByIdAsync(scheduleDto.ServiceId);
            if (service == null)
                return ResultDto<DoctorScheduleDto>.Failure("Service not found");
            
            // Validate room exists
            var room = await _unitOfWork.Repository<Room>().GetByIdAsync(scheduleDto.RoomId);
            if (room == null)
                return ResultDto<DoctorScheduleDto>.Failure("Room not found");
            
            // Validate slot exists
            var slot = await _unitOfWork.Repository<Slot>().GetByIdAsync(scheduleDto.SlotId);
            if (slot == null)
                return ResultDto<DoctorScheduleDto>.Failure("Time slot not found");
            
            // Check if the schedule already exists
            var existingSchedule = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .AnyAsync(ds =>
                    ds.DoctorId == scheduleDto.DoctorId &&
                    ds.SlotId == scheduleDto.SlotId &&
                    ds.DayOfWeek == scheduleDto.DayOfWeek);
            
            if (existingSchedule)
                return ResultDto<DoctorScheduleDto>.Failure("Doctor already has a schedule for this time slot and day");
            
            // Check if room is available for this slot and day
            var roomOccupied = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .AnyAsync(ds =>
                    ds.RoomId == scheduleDto.RoomId &&
                    ds.SlotId == scheduleDto.SlotId &&
                    ds.DayOfWeek == scheduleDto.DayOfWeek);
            
            if (roomOccupied)
                return ResultDto<DoctorScheduleDto>.Failure("Room is already occupied for this time slot and day");
            
            // Create new schedule
            var schedule = _mapper.Map<DoctorSchedule>(scheduleDto);
            
            await _unitOfWork.Repository<DoctorSchedule>().AddAsync(schedule);
            await _unitOfWork.SaveChangesAsync();
            
            // Load complete schedule with related entities
            var createdSchedule = await _unitOfWork.Repository<DoctorSchedule>().Query()
                .Include(ds => ds.Doctor)
                    .ThenInclude(d => d.DoctorNavigation)
                .Include(ds => ds.Service)
                .Include(ds => ds.Room)
                .Include(ds => ds.Slot)
                .FirstOrDefaultAsync(ds => ds.DoctorScheduleId == schedule.DoctorScheduleId);
            
            var scheduleResponseDto = _mapper.Map<DoctorScheduleDto>(createdSchedule);
            scheduleResponseDto.IsAvailable = true; // New schedule is always available
            
            return ResultDto<DoctorScheduleDto>.Success(scheduleResponseDto, "Doctor schedule created successfully");
        }

        public async Task<ResultDto<bool>> DeleteDoctorScheduleAsync(int scheduleId)
        {
            var schedule = await _unitOfWork.Repository<DoctorSchedule>().GetByIdAsync(scheduleId);
            
            if (schedule == null)
                return ResultDto<bool>.Failure("Schedule not found");
            
            // Check if schedule has any reservations
            var hasReservations = await _unitOfWork.Repository<Reservation>().Query()
                .Include(r => r.DoctorSchedules)
                .AnyAsync(r => 
                    r.DoctorSchedules.Any(ds => ds.DoctorScheduleId == scheduleId) && 
                    (r.Status == "Đã xác nhận" || r.Status == "Chờ thanh toán"));
            
            if (hasReservations)
                return ResultDto<bool>.Failure("Cannot delete schedule with active reservations");
            
            _unitOfWork.Repository<DoctorSchedule>().Delete(schedule);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Schedule deleted successfully");
        }

        public async Task<ResultDto<CertificationDto>> AddCertificationAsync(int doctorId, string url, string? description)
        {
            var doctor = await _unitOfWork.Doctors.GetByIdAsync(doctorId);
            
            if (doctor == null)
                return ResultDto<CertificationDto>.Failure("Doctor not found");
            
            var certification = new Certification
            {
                DoctorId = doctorId,
                CertificationUrl = url,
                Description = description
            };
            
            await _unitOfWork.Repository<Certification>().AddAsync(certification);
            await _unitOfWork.SaveChangesAsync();
            
            var certificationDto = _mapper.Map<CertificationDto>(certification);
            return ResultDto<CertificationDto>.Success(certificationDto, "Certification added successfully");
        }

        public async Task<ResultDto<bool>> DeleteCertificationAsync(int certificationId)
        {
            var certification = await _unitOfWork.Repository<Certification>().GetByIdAsync(certificationId);
            
            if (certification == null)
                return ResultDto<bool>.Failure("Certification not found");
            
            _unitOfWork.Repository<Certification>().Delete(certification);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Certification deleted successfully");
        }
    }
} 