using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    public class ServicesController : BaseApiController
    {
        private readonly IServiceService _serviceService;

        public ServicesController(IServiceService serviceService)
        {
            _serviceService = serviceService;
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<IActionResult> GetServices([FromQuery] int pageIndex = 1, [FromQuery] int pageSize = 10, [FromQuery] string? searchTerm = null)
        {
            var result = await _serviceService.GetPaginatedServicesAsync(pageIndex, pageSize, searchTerm);
            return HandlePagedResult(result);
        }

        [HttpGet("{id}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetService(int id)
        {
            var result = await _serviceService.GetServiceByIdAsync(id);
            return HandleResult(result);
        }

        [HttpGet("{id}/details")]
        [AllowAnonymous]
        public async Task<IActionResult> GetServiceDetails(int id)
        {
            var result = await _serviceService.GetServiceDetailsAsync(id);
            return HandleResult(result);
        }

        [HttpGet("specialty/{specialtyId}")]
        [AllowAnonymous]
        public async Task<IActionResult> GetServicesBySpecialty(int specialtyId)
        {
            var result = await _serviceService.GetServicesBySpecialtyAsync(specialtyId);
            return HandleResult(result);
        }

        [HttpGet("{parentId}/children")]
        [AllowAnonymous]
        public async Task<IActionResult> GetChildServices(int parentId)
        {
            var result = await _serviceService.GetChildServicesAsync(parentId);
            return HandleResult(result);
        }

        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateService(ServiceCreateDto serviceDto)
        {
            var result = await _serviceService.CreateServiceAsync(serviceDto);
            return HandleResult(result);
        }

        [HttpPut("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateService(int id, ServiceUpdateDto serviceDto)
        {
            var result = await _serviceService.UpdateServiceAsync(id, serviceDto);
            return HandleResult(result);
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteService(int id)
        {
            var result = await _serviceService.DeleteServiceAsync(id);
            return HandleResult(result);
        }

        [HttpGet("devices")]
        [AllowAnonymous]
        public async Task<IActionResult> GetDevices()
        {
            var result = await _serviceService.GetAllDevicesAsync();
            return HandleResult(result);
        }
    }
} 