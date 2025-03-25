using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Server.Controllers
{
    public class UsersController : BaseApiController
    {
        private readonly IUserService _userService;

        public UsersController(IUserService userService)
        {
            _userService = userService;
        }

        [HttpGet]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetUsers([FromQuery] int pageIndex = 1, [FromQuery] int pageSize = 10, [FromQuery] string? searchTerm = null)
        {
            var result = await _userService.GetPaginatedUsersAsync(pageIndex, pageSize, searchTerm);
            return HandlePagedResult(result);
        }

        [HttpGet("{id}")]
        [Authorize]
        public async Task<IActionResult> GetUser(int id)
        {
            var result = await _userService.GetUserByIdAsync(id);
            return HandleResult(result);
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<IActionResult> CreateUser(UserCreateDto userDto)
        {
            var result = await _userService.CreateUserAsync(userDto);
            return HandleResult(result);
        }

        [HttpPut("{id}")]
        [Authorize]
        public async Task<IActionResult> UpdateUser(int id, UserUpdateDto userDto)
        {
            var result = await _userService.UpdateUserAsync(id, userDto);
            return HandleResult(result);
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteUser(int id)
        {
            var result = await _userService.DeleteUserAsync(id);
            return HandleResult(result);
        }

        [HttpPost("login")]
        [AllowAnonymous]
        public async Task<IActionResult> Login(LoginRequestDto loginRequest)
        {
            var result = await _userService.LoginAsync(loginRequest);
            return HandleResult(result);
        }

        [HttpPost("refresh-token")]
        [AllowAnonymous]
        public async Task<IActionResult> RefreshToken(RefreshTokenRequestDto request)
        {
            var result = await _userService.RefreshTokenAsync(request.RefreshToken);
            return HandleResult(result);
        }

        [HttpPost("revoke-token")]
        [Authorize]
        public async Task<IActionResult> RevokeToken(RefreshTokenRequestDto request)
        {
            var result = await _userService.RevokeTokenAsync(request.RefreshToken);
            return HandleResult(result);
        }

        [HttpPut("{id}/toggle-status")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> ToggleUserStatus(int id)
        {
            var result = await _userService.ToggleUserStatusAsync(id);
            return HandleResult(result);
        }

        [HttpPut("{id}/assign-roles")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> AssignRolesToUser(int id, [FromBody] List<int> roleIds)
        {
            var result = await _userService.AssignRolesToUserAsync(id, roleIds);
            return HandleResult(result);
        }
    }
} 