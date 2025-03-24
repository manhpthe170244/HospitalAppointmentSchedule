using AutoMapper;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.DTOs.SpecialtyDTOs;

namespace HospitalAppointmentShedule.Services.Profile
{
    public class ServiceProfile : AutoMapper.Profile
    {
        public ServiceProfile()
        {
            CreateMap<Service, ServiceDTO>()
                .ForMember(dest => dest.ServiceId, opt => opt.MapFrom(src => src.ServiceId))
                .ForMember(dest => dest.ServiceName, opt => opt.MapFrom(src => src.ServiceName))
                .ForMember(dest => dest.Overview, opt => opt.MapFrom(src => src.Overview))
                .ForMember(dest => dest.Process, opt => opt.MapFrom(src => src.Process))
                .ForMember(dest => dest.TreatmentTechniques, opt => opt.MapFrom(src => src.TreatmentTechniques))
                .ForMember(dest => dest.Price, opt => opt.MapFrom(src => src.Price))
                .ForMember(dest => dest.EstimatedTime, opt => opt.MapFrom(src => src.EstimatedTime))
                .ForMember(dest => dest.IsPrepayment, opt => opt.MapFrom(src => src.IsPrepayment))
                .ForMember(dest => dest.ParentServiceId, opt => opt.MapFrom(src => src.ParentServiceId))
                .ForMember(dest => dest.SpecialtyId, opt => opt.MapFrom(src => src.SpecialtyId))
                .ForMember(dest => dest.Image, opt => opt.MapFrom(src => src.Image))
                .ForMember(dest => dest.ParentService, opt => opt.MapFrom(src => src.ParentService))
                .ForMember(dest => dest.Specialty, opt => opt.MapFrom(src => src.Specialty));

            CreateMap<ServiceDTO, Service>()
                .ForMember(dest => dest.ServiceId, opt => opt.MapFrom(src => src.ServiceId))
                .ForMember(dest => dest.ServiceName, opt => opt.MapFrom(src => src.ServiceName))
                .ForMember(dest => dest.Overview, opt => opt.MapFrom(src => src.Overview))
                .ForMember(dest => dest.Process, opt => opt.MapFrom(src => src.Process))
                .ForMember(dest => dest.TreatmentTechniques, opt => opt.MapFrom(src => src.TreatmentTechniques))
                .ForMember(dest => dest.Price, opt => opt.MapFrom(src => src.Price))
                .ForMember(dest => dest.EstimatedTime, opt => opt.MapFrom(src => src.EstimatedTime))
                .ForMember(dest => dest.IsPrepayment, opt => opt.MapFrom(src => src.IsPrepayment))
                .ForMember(dest => dest.ParentServiceId, opt => opt.MapFrom(src => src.ParentServiceId))
                .ForMember(dest => dest.SpecialtyId, opt => opt.MapFrom(src => src.SpecialtyId))
                .ForMember(dest => dest.Image, opt => opt.MapFrom(src => src.Image));

            CreateMap<Specialty, SpecialtyDTO>()
                .ForMember(dest => dest.SpecialtyId, opt => opt.MapFrom(src => src.SpecialtyId))
                .ForMember(dest => dest.SpecialtyName, opt => opt.MapFrom(src => src.SpecialtyName))
                .ForMember(dest => dest.SpecialtyDescription, opt => opt.MapFrom(src => src.SpecialtyDescription))
                .ForMember(dest => dest.Image, opt => opt.MapFrom(src => src.Image));
        }
    }
}