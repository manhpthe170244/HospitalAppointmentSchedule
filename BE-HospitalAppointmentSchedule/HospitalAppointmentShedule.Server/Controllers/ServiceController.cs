using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.IService;
using Microsoft.AspNetCore.Mvc;
using HospitalAppointmentShedule.Infrastructure.UnitOfWork;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Services.Services;
namespace HospitalAppointmentShedule.Server.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ServiceController : ControllerBase
    {
        private readonly IServiceService _serviceService;

        public ServiceController(IServiceService serviceService)
        {
            _serviceService = serviceService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ServiceDTO>>> GetAllServices()
        {
            var services = await _serviceService.GetAllServicesAsync();
            return Ok(services);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ServiceDTO>> GetServiceById(int id)
        {
            var service = await _serviceService.GetServiceByIdAsync(id);
            if (service == null)
            {
                return NotFound();
            }
            return Ok(service);
        }

        [HttpPost]
        public async Task<ActionResult<ServiceDTO>> CreateService(ServiceDTO createServiceDto)
        {
            var createdService = await _serviceService.CreateServiceAsync(createServiceDto);
            return CreatedAtAction(nameof(GetServiceById), new { id = createdService.ServiceId }, createdService);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateService(int id, ServiceDTO updateServiceDto)
        {
            var updatedService = await _serviceService.UpdateServiceAsync(id, updateServiceDto);
            if (updatedService == null)
            {
                return NotFound();
            }
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteService(int id)
        {
            await _serviceService.DeleteServiceAsync(id);
            return NoContent();
        }
    }
}