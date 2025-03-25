using HospitalAppointmentShedule.Services.DTOs;
using Microsoft.AspNetCore.Mvc;

namespace HospitalAppointmentShedule.Server.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public abstract class BaseApiController : ControllerBase
    {
        protected IActionResult HandleResult<T>(ResultDto<T> result)
        {
            if (result == null)
                return NotFound();

            if (result.IsSuccess && result.Data == null)
                return NotFound();

            if (result.IsSuccess)
                return Ok(result);

            if (result.Errors.Count > 0)
                return BadRequest(result);

            return StatusCode(500, result);
        }

        protected IActionResult HandlePagedResult<T>(ResultDto<PaginatedResultDto<T>> result)
        {
            if (result == null)
                return NotFound();

            if (result.IsSuccess && result.Data == null)
                return NotFound();

            if (result.IsSuccess)
                return Ok(result);

            if (result.Errors.Count > 0)
                return BadRequest(result);

            return StatusCode(500, result);
        }
    }
} 