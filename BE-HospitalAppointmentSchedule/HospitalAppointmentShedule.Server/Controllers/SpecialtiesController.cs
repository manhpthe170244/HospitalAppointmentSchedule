using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    public class SpecialtiesController : BaseApiController
    {
        private readonly IServiceService _serviceService;

        public SpecialtiesController(IServiceService serviceService)
        {
            _serviceService = serviceService;
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<IActionResult> GetSpecialties()
        {
            var result = await _serviceService.GetAllSpecialtiesAsync();
            return HandleResult(result);
        }

        [HttpGet("{id}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetSpecialty(int id)
        {
            var result = await _serviceService.GetSpecialtyByIdAsync(id);
            return HandleResult(result);
        }

        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateSpecialty(SpecialtyCreateDto specialtyDto)
        {
            var result = await _serviceService.CreateSpecialtyAsync(specialtyDto);
            return HandleResult(result);
        }

        [HttpPut("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateSpecialty(int id, SpecialtyUpdateDto specialtyDto)
        {
            var result = await _serviceService.UpdateSpecialtyAsync(id, specialtyDto);
            return HandleResult(result);
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteSpecialty(int id)
        {
            var result = await _serviceService.DeleteSpecialtyAsync(id);
            return HandleResult(result);
        }
    }
} 