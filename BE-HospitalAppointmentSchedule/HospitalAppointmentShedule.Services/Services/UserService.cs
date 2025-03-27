using AutoMapper;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Services.DTOs;
using HospitalAppointmentShedule.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using BCrypt.Net;

namespace HospitalAppointmentShedule.Services.Services
{
    public class UserService : IUserService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IMapper _mapper;
        private readonly IConfiguration _configuration;
        private readonly ILogger<UserService> _logger;

        public UserService(IUnitOfWork unitOfWork, IMapper mapper, IConfiguration configuration, ILogger<UserService> logger)
        {
            _unitOfWork = unitOfWork;
            _mapper = mapper;
            _configuration = configuration;
            _logger = logger;
        }

        public async Task<ResultDto<UserDto>> GetUserByIdAsync(int userId)
        {
            var user = await _unitOfWork.Users.GetUserWithRolesAsync(userId);
            
            if (user == null)
                return ResultDto<UserDto>.Failure("User not found");
            
            var userDto = _mapper.Map<UserDto>(user);
            return ResultDto<UserDto>.Success(userDto);
        }

        public async Task<ResultDto<List<UserDto>>> GetAllUsersAsync()
        {
            var users = await _unitOfWork.Users.Query()
                .Include(u => u.Roles)
                .ToListAsync();
            
            var userDtos = _mapper.Map<List<UserDto>>(users);
            return ResultDto<List<UserDto>>.Success(userDtos);
        }

        public async Task<ResultDto<PaginatedResultDto<UserDto>>> GetPaginatedUsersAsync(int pageIndex, int pageSize, string? searchTerm = null)
        {
            var query = _unitOfWork.Users.Query().Include(u => u.Roles).AsQueryable();
            
            if (!string.IsNullOrEmpty(searchTerm))
            {
                query = query.Where(u => 
                    u.UserName.Contains(searchTerm) || 
                    u.Email.Contains(searchTerm) || 
                    u.Phone.Contains(searchTerm));
            }
            
            var totalCount = await query.CountAsync();
            var users = await query
                .OrderBy(u => u.UserId)
                .Skip((pageIndex - 1) * pageSize)
                .Take(pageSize)
                .ToListAsync();
            
            var userDtos = _mapper.Map<List<UserDto>>(users);
            
            var paginatedResult = PaginatedResultDto<UserDto>.Create(
                userDtos,
                totalCount,
                pageIndex,
                pageSize);
            
            return ResultDto<PaginatedResultDto<UserDto>>.Success(paginatedResult);
        }

        public async Task<ResultDto<UserDto>> CreateUserAsync(UserCreateDto userDto)
        {
            // Check if email already exists
            if (!await _unitOfWork.Users.IsEmailUniqueAsync(userDto.Email))
                return ResultDto<UserDto>.Failure("Email is already in use");
            
            // Check if phone already exists
            if (!await _unitOfWork.Users.IsPhoneUniqueAsync(userDto.Phone))
                return ResultDto<UserDto>.Failure("Phone number is already in use");
            
            var user = _mapper.Map<User>(userDto);
            
            // Hash password
            user.Password = HashPassword(userDto.Password);
            
            // If no roles provided, assign User role by default
            if (userDto.RoleIds.Count == 0)
            {
                var userRole = await _unitOfWork.Repository<Role>().Query()
                    .FirstOrDefaultAsync(r => r.RoleName == "User");
                
                if (userRole != null)
                {
                    user.Roles = new List<Role> { userRole };
                }
            }
            else
            {
                // Get roles
                var roles = await _unitOfWork.Repository<Role>().Query()
                    .Where(r => userDto.RoleIds.Contains(r.RoleId))
                    .ToListAsync();
                
                user.Roles = roles;
            }
            
            await _unitOfWork.Users.AddAsync(user);
            await _unitOfWork.SaveChangesAsync();
            
            var userReturnDto = _mapper.Map<UserDto>(user);
            return ResultDto<UserDto>.Success(userReturnDto);
        }

        public async Task<ResultDto<UserDto>> UpdateUserAsync(int userId, UserUpdateDto userDto)
        {
            var user = await _unitOfWork.Users.GetUserWithRolesAsync(userId);
            
            if (user == null)
                return ResultDto<UserDto>.Failure("User not found");
            
            // Check email uniqueness if email is being updated
            if (userDto.Email != null && user.Email != userDto.Email)
            {
                if (!await _unitOfWork.Users.IsEmailUniqueAsync(userDto.Email, userId))
                    return ResultDto<UserDto>.Failure("Email is already in use");
            }
            
            // Check phone uniqueness if phone is being updated
            if (userDto.Phone != null && user.Phone != userDto.Phone)
            {
                if (!await _unitOfWork.Users.IsPhoneUniqueAsync(userDto.Phone, userId))
                    return ResultDto<UserDto>.Failure("Phone number is already in use");
            }
            
            // Update user properties
            _mapper.Map(userDto, user);
            
            // Update roles if provided
            if (userDto.RoleIds != null && userDto.RoleIds.Any())
            {
                var roles = await _unitOfWork.Repository<Role>().Query()
                    .Where(r => userDto.RoleIds.Contains(r.RoleId))
                    .ToListAsync();
                
                user.Roles.Clear();
                foreach (var role in roles)
                {
                    user.Roles.Add(role);
                }
            }
            
            _unitOfWork.Users.Update(user);
            await _unitOfWork.SaveChangesAsync();
            
            var userReturnDto = _mapper.Map<UserDto>(user);
            return ResultDto<UserDto>.Success(userReturnDto);
        }

        public async Task<ResultDto<bool>> DeleteUserAsync(int userId)
        {
            var user = await _unitOfWork.Users.GetByIdAsync(userId);
            
            if (user == null)
                return ResultDto<bool>.Failure("User not found");
            
            _unitOfWork.Users.Delete(user);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "User deleted successfully");
        }

        public async Task<ResultDto<bool>> ToggleUserStatusAsync(int userId)
        {
            var user = await _unitOfWork.Users.GetByIdAsync(userId);
            
            if (user == null)
                return ResultDto<bool>.Failure("User not found");
            
            user.IsVerify = !user.IsVerify;
            
            _unitOfWork.Users.Update(user);
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, $"User status updated to {(user.IsVerify ?? false ? "active" : "inactive")}");
        }

        public async Task<ResultDto<bool>> AssignRolesToUserAsync(int userId, List<int> roleIds)
        {
            var user = await _unitOfWork.Users.GetUserWithRolesAsync(userId);
            
            if (user == null)
                return ResultDto<bool>.Failure("User not found");
            
            var roles = await _unitOfWork.Repository<Role>().Query()
                .Where(r => roleIds.Contains(r.RoleId))
                .ToListAsync();
            
            if (roles.Count != roleIds.Count)
                return ResultDto<bool>.Failure("One or more roles do not exist");
            
            user.Roles.Clear();
            foreach (var role in roles)
            {
                user.Roles.Add(role);
            }
            
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Roles assigned successfully");
        }

        public async Task<ResultDto<LoginResponseDto>> LoginAsync(LoginRequestDto loginRequest)
        {
            try
            {
                _logger.LogInformation($"Attempting login for user: {loginRequest.EmailOrPhone}");
                
                // Normalize email/phone input
                var searchTerm = loginRequest.EmailOrPhone?.Trim();
                _logger.LogDebug($"Normalized search term: {searchTerm}");

                // Try to find user by exact match first
                var user = await _unitOfWork.Users.Query()
                    .FirstOrDefaultAsync(u => u.Email == searchTerm || u.Phone == searchTerm);

                // If not found, try partial match for email (in case @ is missing)
                if (user == null && searchTerm.Contains("@"))
                {
                    var emailParts = searchTerm.Split('@');
                    if (emailParts.Length == 2)
                    {
                        var partialEmail = emailParts[0] + emailParts[1];
                        _logger.LogDebug($"Trying partial email match: {partialEmail}");
                        user = await _unitOfWork.Users.Query()
                            .FirstOrDefaultAsync(u => u.Email.Replace("@", "") == partialEmail);
                    }
                }
                
                if (user == null)
                {
                    _logger.LogWarning($"Login failed: User not found for {searchTerm}");
                    return ResultDto<LoginResponseDto>.Failure("Thông tin đăng nhập không chính xác");
                }
                
                _logger.LogInformation($"User found: {user.UserId}, Email: {user.Email}, verifying password");
                
                // Verify password
                if (!VerifyPassword(loginRequest.Password, user.Password))
                {
                    _logger.LogWarning($"Login failed: Invalid password for user {user.UserId}");
                    return ResultDto<LoginResponseDto>.Failure("Thông tin đăng nhập không chính xác");
                }
                
                // Check if user is verified/active
                if (user.IsVerify == false)
                {
                    _logger.LogWarning($"Login failed: Account not active for user {user.UserId}");
                    return ResultDto<LoginResponseDto>.Failure("Tài khoản chưa được kích hoạt. Vui lòng liên hệ hỗ trợ.");
                }
                
                // Load roles for token generation
                user = await _unitOfWork.Users.GetUserWithRolesAsync(user.UserId);
                
                // Generate JWT token
                var token = GenerateJwtToken(user);
                var refreshToken = GenerateRefreshToken();
                
                // Save refresh token to database
                var refreshTokenEntity = new RefreshToken
                {
                    Token = refreshToken,
                    UserId = user.UserId,
                    Created = DateTime.UtcNow,
                    Expires = DateTime.UtcNow.AddDays(7)
                };
                
                await _unitOfWork.Repository<RefreshToken>().AddAsync(refreshTokenEntity);
                await _unitOfWork.SaveChangesAsync();
                
                var userDto = _mapper.Map<UserDto>(user);
                
                var response = new LoginResponseDto
                {
                    User = userDto,
                    AccessToken = token,
                    RefreshToken = refreshToken,
                    ExpiresAt = DateTime.UtcNow.AddHours(1)
                };
                
                _logger.LogInformation($"Login successful for user {user.UserId}");
                return ResultDto<LoginResponseDto>.Success(response, "Đăng nhập thành công");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Error during login for user {loginRequest.EmailOrPhone}");
                return ResultDto<LoginResponseDto>.Failure("Có lỗi xảy ra trong quá trình đăng nhập");
            }
        }

        public async Task<ResultDto<LoginResponseDto>> RefreshTokenAsync(string refreshToken)
        {
            var refreshTokenEntity = await _unitOfWork.Repository<RefreshToken>().Query()
                .Include(rt => rt.User)
                    .ThenInclude(u => u.Roles)
                .FirstOrDefaultAsync(rt => rt.Token == refreshToken);
            
            if (refreshTokenEntity == null)
                return ResultDto<LoginResponseDto>.Failure("Invalid refresh token");
            
            if (refreshTokenEntity.Revoked != null)
                return ResultDto<LoginResponseDto>.Failure("Refresh token has been revoked");
            
            if (refreshTokenEntity.Expires < DateTime.UtcNow)
                return ResultDto<LoginResponseDto>.Failure("Refresh token has expired");
            
            var user = refreshTokenEntity.User;
            
            if (user.IsVerify == false)
                return ResultDto<LoginResponseDto>.Failure("Account is not active");
            
            var token = GenerateJwtToken(user);
            var newRefreshToken = GenerateRefreshToken();
            
            // Save new refresh token and revoke old one
            refreshTokenEntity.Revoked = DateTime.UtcNow;
            
            var newRefreshTokenEntity = new RefreshToken
            {
                Token = newRefreshToken,
                UserId = user.UserId,
                Created = DateTime.UtcNow,
                Expires = DateTime.UtcNow.AddDays(7)
            };
            
            await _unitOfWork.Repository<RefreshToken>().AddAsync(newRefreshTokenEntity);
            await _unitOfWork.SaveChangesAsync();
            
            var userDto = _mapper.Map<UserDto>(user);
            
            var response = new LoginResponseDto
            {
                User = userDto,
                AccessToken = token,
                RefreshToken = newRefreshToken,
                ExpiresAt = DateTime.UtcNow.AddHours(1) // Token expiration time
            };
            
            return ResultDto<LoginResponseDto>.Success(response, "Token refreshed successfully");
        }

        public async Task<ResultDto<bool>> RevokeTokenAsync(string refreshToken)
        {
            var refreshTokenEntity = await _unitOfWork.Repository<RefreshToken>().Query()
                .FirstOrDefaultAsync(rt => rt.Token == refreshToken);
            
            if (refreshTokenEntity == null)
                return ResultDto<bool>.Failure("Invalid refresh token");
            
            if (refreshTokenEntity.Revoked != null)
                return ResultDto<bool>.Failure("Refresh token has already been revoked");
            
            refreshTokenEntity.Revoked = DateTime.UtcNow;
            await _unitOfWork.SaveChangesAsync();
            
            return ResultDto<bool>.Success(true, "Token revoked successfully");
        }

        #region Helper Methods
        private string HashPassword(string password)
        {
            return BCrypt.Net.BCrypt.HashPassword(password, 11);
        }

        private bool VerifyPassword(string inputPassword, string storedHash)
        {
            try
            {
                _logger.LogInformation("Starting password verification");
                
                if (string.IsNullOrEmpty(storedHash) || !storedHash.StartsWith("$2a$"))
                {
                    _logger.LogWarning("Invalid BCrypt hash format in database");
                    return false;
                }

                var result = BCrypt.Net.BCrypt.Verify(inputPassword, storedHash);

                if (!result)
                {
                    _logger.LogWarning("Password verification failed: Hash mismatch");
                }
                else
                {
                    _logger.LogInformation("Password verification successful");
                }

                return result;
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error during password verification");
                return false;
            }
        }

        private string GenerateJwtToken(User user)
        {
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));
            var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, user.UserId.ToString()),
                new Claim(ClaimTypes.Email, user.Email),
                new Claim(ClaimTypes.Name, user.UserName)
            };

            // Add role claims
            foreach (var role in user.Roles)
            {
                claims.Add(new Claim(ClaimTypes.Role, role.RoleName));
            }

            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:Issuer"],
                audience: _configuration["JWT:Audience"],
                claims: claims,
                expires: DateTime.UtcNow.AddMinutes(Convert.ToDouble(_configuration["JWT:ExpirationInMinutes"])),
                signingCredentials: credentials
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        private string GenerateRefreshToken()
        {
            var randomNumber = new byte[32];
            using (var rng = RandomNumberGenerator.Create())
            {
                rng.GetBytes(randomNumber);
            }
            return Convert.ToBase64String(randomNumber);
        }
        #endregion
    }
} 