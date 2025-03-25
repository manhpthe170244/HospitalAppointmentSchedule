using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    public class DoctorsController : BaseApiController
    {
        private readonly IDoctorService _doctorService;

        public DoctorsController(IDoctorService doctorService)
        {
            _doctorService = doctorService;
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctors([FromQuery] int pageIndex = 1, [FromQuery] int pageSize = 10, [FromQuery] string? searchTerm = null)
        {
            var result = await _doctorService.GetPaginatedDoctorsAsync(pageIndex, pageSize, searchTerm);
            return HandlePagedResult(result);
        }

        [HttpGet("{id}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctor(int id)
        {
            var result = await _doctorService.GetDoctorByIdAsync(id);
            return HandleResult(result);
        }

        [HttpGet("{id}/details")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctorDetails(int id)
        {
            var result = await _doctorService.GetDoctorDetailsAsync(id);
            return HandleResult(result);
        }

        [HttpGet("specialty/{specialtyId}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctorsBySpecialty(int specialtyId)
        {
            var result = await _doctorService.GetDoctorsBySpecialtyAsync(specialtyId);
            return HandleResult(result);
        }

        [HttpGet("service/{serviceId}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctorsByService(int serviceId)
        {
            var result = await _doctorService.GetDoctorsByServiceAsync(serviceId);
            return HandleResult(result);
        }

        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateDoctor(DoctorCreateDto doctorDto)
        {
            var result = await _doctorService.CreateDoctorAsync(doctorDto);
            return HandleResult(result);
        }

        [HttpPut("{id}")]
        [Authorize(Roles = "Admin,Doctor")]
        public async Task<IActionResult> UpdateDoctor(int id, DoctorUpdateDto doctorDto)
        {
            var result = await _doctorService.UpdateDoctorAsync(id, doctorDto);
            return HandleResult(result);
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteDoctor(int id)
        {
            var result = await _doctorService.DeleteDoctorAsync(id);
            return HandleResult(result);
        }

        [HttpGet("{id}/schedules")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDoctorSchedules(int id, [FromQuery] DateTime? date = null)
        {
            var result = await _doctorService.GetDoctorSchedulesAsync(id, date);
            return HandleResult(result);
        }

        [HttpPost("schedules")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateDoctorSchedule(DoctorScheduleCreateDto scheduleDto)
        {
            var result = await _doctorService.CreateDoctorScheduleAsync(scheduleDto);
            return HandleResult(result);
        }

        [HttpDelete("schedules/{scheduleId}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteDoctorSchedule(int scheduleId)
        {
            var result = await _doctorService.DeleteDoctorScheduleAsync(scheduleId);
            return HandleResult(result);
        }

        [HttpPost("{id}/certifications")]
        [Authorize(Roles = "Admin,Doctor")]
        public async Task<IActionResult> AddCertification(int id, [FromBody] CertificationDto certificationDto)
        {
            var result = await _doctorService.AddCertificationAsync(id, certificationDto.CertificationUrl, certificationDto.Description);
            return HandleResult(result);
        }

        [HttpDelete("certifications/{certificationId}")]
        [Authorize(Roles = "Admin,Doctor")]
        public async Task<IActionResult> DeleteCertification(int certificationId)
        {
            var result = await _doctorService.DeleteCertificationAsync(certificationId);
            return HandleResult(result);
        }
    }
} 