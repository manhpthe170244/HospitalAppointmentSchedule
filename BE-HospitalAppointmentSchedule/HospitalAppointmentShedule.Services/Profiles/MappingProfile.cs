using AutoMapper;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using System.Linq;

namespace HospitalAppointmentShedule.Services.Profiles
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            // User mappings
            CreateMap<User, UserDto>()
                .ForMember(dest => dest.Roles, opt => opt.MapFrom(src => src.Roles.Select(r => r.RoleName).ToList()));
            CreateMap<UserCreateDto, User>();
            CreateMap<UserUpdateDto, User>()
                .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

            // Doctor mappings
            CreateMap<Doctor, DoctorDto>()
                .ForMember(dest => dest.UserName, opt => opt.MapFrom(src => src.DoctorNavigation.UserName))
                .ForMember(dest => dest.Email, opt => opt.MapFrom(src => src.DoctorNavigation.Email))
                .ForMember(dest => dest.Phone, opt => opt.MapFrom(src => src.DoctorNavigation.Phone))
                .ForMember(dest => dest.AvatarUrl, opt => opt.MapFrom(src => src.DoctorNavigation.AvatarUrl))
                .ForMember(dest => dest.Specialties, opt => opt.MapFrom(src => src.Specialties.Select(s => s.SpecialtyName).ToList()));
            
            CreateMap<Doctor, DoctorDetailDto>()
                .IncludeBase<Doctor, DoctorDto>()
                .ForMember(dest => dest.Services, opt => opt.MapFrom(src => src.Services))
                .ForMember(dest => dest.Schedules, opt => opt.MapFrom(src => src.DoctorSchedules));

            // Certification mapping
            CreateMap<Certification, CertificationDto>();

            // Service mappings
            CreateMap<Service, ServiceDto>()
                .ForMember(dest => dest.SpecialtyName, opt => opt.MapFrom(src => src.Specialty.SpecialtyName))
                .ForMember(dest => dest.ParentServiceName, opt => opt.MapFrom(src => src.ParentService != null ? src.ParentService.ServiceName : null));
            
            CreateMap<Service, ServiceDetailDto>()
                .IncludeBase<Service, ServiceDto>()
                .ForMember(dest => dest.ChildServices, opt => opt.MapFrom(src => src.InverseParentService))
                .ForMember(dest => dest.Devices, opt => opt.MapFrom(src => src.Devices))
                .ForMember(dest => dest.Doctors, opt => opt.MapFrom(src => src.Doctors));
            
            CreateMap<ServiceCreateDto, Service>();
            CreateMap<ServiceUpdateDto, Service>()
                .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

            // Device mapping
            CreateMap<Device, DeviceDto>();

            // Specialty mappings
            CreateMap<Specialty, SpecialtyDto>();
            CreateMap<SpecialtyCreateDto, Specialty>();
            CreateMap<SpecialtyUpdateDto, Specialty>()
                .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

            // DoctorSchedule mapping
            CreateMap<DoctorSchedule, DoctorScheduleDto>()
                .ForMember(dest => dest.DoctorName, opt => opt.MapFrom(src => src.Doctor.DoctorNavigation.UserName))
                .ForMember(dest => dest.ServiceName, opt => opt.MapFrom(src => src.Service.ServiceName))
                .ForMember(dest => dest.RoomName, opt => opt.MapFrom(src => src.Room.RoomName))
                .ForMember(dest => dest.SlotStartTime, opt => opt.MapFrom(src => src.Slot.SlotStartTime))
                .ForMember(dest => dest.SlotEndTime, opt => opt.MapFrom(src => src.Slot.SlotEndTime))
                .ForMember(dest => dest.IsAvailable, opt => opt.MapFrom(src => true)); // This would be calculated in the service

            CreateMap<DoctorScheduleCreateDto, DoctorSchedule>();

            // Reservation mappings
            CreateMap<Reservation, ReservationDto>()
                .ForMember(dest => dest.PatientName, opt => opt.MapFrom(src => src.Patient.PatientNavigation.UserName))
                .ForMember(dest => dest.ServiceName, opt => opt.MapFrom(src => src.DoctorSchedules.FirstOrDefault().Service.ServiceName))
                .ForMember(dest => dest.DoctorName, opt => opt.MapFrom(src => src.DoctorSchedules.FirstOrDefault().Doctor.DoctorNavigation.UserName))
                .ForMember(dest => dest.SlotTime, opt => opt.MapFrom(src => 
                    $"{src.DoctorSchedules.FirstOrDefault().Slot.SlotStartTime.ToString(@"hh\:mm")} - {src.DoctorSchedules.FirstOrDefault().Slot.SlotEndTime.ToString(@"hh\:mm")}"))
                .ForMember(dest => dest.RoomName, opt => opt.MapFrom(src => src.DoctorSchedules.FirstOrDefault().Room.RoomName))
                .ForMember(dest => dest.HasPaid, opt => opt.MapFrom(src => src.Payments.Any(p => p.PaymentStatus == "Thành công")));

            CreateMap<Reservation, ReservationDetailDto>()
                .IncludeBase<Reservation, ReservationDto>();

            CreateMap<ReservationCreateDto, Reservation>();
            CreateMap<ReservationUpdateDto, Reservation>()
                .ForAllMembers(opts => opts.Condition((src, dest, srcMember) => srcMember != null));

            // Patient mappings
            CreateMap<Patient, PatientDto>()
                .ForMember(dest => dest.UserName, opt => opt.MapFrom(src => src.PatientNavigation.UserName))
                .ForMember(dest => dest.Email, opt => opt.MapFrom(src => src.PatientNavigation.Email))
                .ForMember(dest => dest.Phone, opt => opt.MapFrom(src => src.PatientNavigation.Phone))
                .ForMember(dest => dest.DateOfBirth, opt => opt.MapFrom(src => src.PatientNavigation.DateOfBirth))
                .ForMember(dest => dest.Gender, opt => opt.MapFrom(src => src.PatientNavigation.Gender))
                .ForMember(dest => dest.Address, opt => opt.MapFrom(src => src.PatientNavigation.Address))
                .ForMember(dest => dest.GuardianName, opt => opt.MapFrom(src => src.Guardian != null ? src.Guardian.UserName : null));

            // Payment mappings
            CreateMap<Payment, PaymentDto>()
                .ForMember(dest => dest.UserName, opt => opt.MapFrom(src => src.User.UserName))
                .ForMember(dest => dest.ReceptionistName, opt => opt.MapFrom(src => src.Receptionist != null ? src.Receptionist.ReceptionistNavigation.UserName : null));

            CreateMap<PaymentCreateDto, Payment>();

            // MedicalRecord mappings
            CreateMap<MedicalRecord, MedicalRecordDto>();
            CreateMap<MedicalRecordCreateDto, MedicalRecord>();

            // Feedback mappings
            CreateMap<Feedback, FeedbackDto>();
            CreateMap<FeedbackCreateDto, Feedback>();
        }
    }
} 