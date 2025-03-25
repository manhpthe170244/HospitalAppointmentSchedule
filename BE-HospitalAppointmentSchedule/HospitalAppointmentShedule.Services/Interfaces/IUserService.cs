using HospitalAppointmentShedule.Services.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Services.Interfaces
{
    public interface IUserService
    {
        Task<ResultDto<UserDto>> GetUserByIdAsync(int userId);
        Task<ResultDto<List<UserDto>>> GetAllUsersAsync();
        Task<ResultDto<PaginatedResultDto<UserDto>>> GetPaginatedUsersAsync(int pageIndex, int pageSize, string? searchTerm = null);
        Task<ResultDto<UserDto>> CreateUserAsync(UserCreateDto userDto);
        Task<ResultDto<UserDto>> UpdateUserAsync(int userId, UserUpdateDto userDto);
        Task<ResultDto<bool>> DeleteUserAsync(int userId);
        Task<ResultDto<bool>> ToggleUserStatusAsync(int userId);
        Task<ResultDto<bool>> AssignRolesToUserAsync(int userId, List<int> roleIds);
        Task<ResultDto<LoginResponseDto>> LoginAsync(LoginRequestDto loginRequest);
        Task<ResultDto<LoginResponseDto>> RefreshTokenAsync(string refreshToken);
        Task<ResultDto<bool>> RevokeTokenAsync(string refreshToken);
    }
} 