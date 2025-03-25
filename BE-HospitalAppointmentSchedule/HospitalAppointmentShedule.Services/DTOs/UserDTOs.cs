using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace HospitalAppointmentShedule.Services.DTOs
{
    public class UserDto
    {
        public int UserId { get; set; }
        public string UserName { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Phone { get; set; } = null!;
        public DateTime? DateOfBirth { get; set; }
        public string? Gender { get; set; }
        public string? Address { get; set; }
        public string? AvatarUrl { get; set; }
        public bool IsVerify { get; set; }
        public List<string> Roles { get; set; } = new List<string>();
    }

    public class UserCreateDto
    {
        [Required]
        [StringLength(50)]
        public string UserName { get; set; } = null!;

        [Required]
        [EmailAddress]
        [StringLength(50)]
        public string Email { get; set; } = null!;

        [Required]
        [StringLength(12)]
        public string Phone { get; set; } = null!;

        [Required]
        [StringLength(300, MinimumLength = 6)]
        public string Password { get; set; } = null!;

        public DateTime? DateOfBirth { get; set; }

        [StringLength(6)]
        public string? Gender { get; set; }

        [StringLength(100)]
        public string? Address { get; set; }

        public string? AvatarUrl { get; set; }

        public List<int> RoleIds { get; set; } = new List<int>();
    }

    public class UserUpdateDto
    {
        [StringLength(50)]
        public string? UserName { get; set; }

        [EmailAddress]
        [StringLength(50)]
        public string? Email { get; set; }

        [StringLength(12)]
        public string? Phone { get; set; }

        public DateTime? DateOfBirth { get; set; }

        [StringLength(6)]
        public string? Gender { get; set; }

        [StringLength(100)]
        public string? Address { get; set; }

        public string? AvatarUrl { get; set; }

        public List<int>? RoleIds { get; set; }
    }

    public class LoginRequestDto
    {
        [Required]
        public string EmailOrPhone { get; set; } = null!;

        [Required]
        public string Password { get; set; } = null!;
    }

    public class LoginResponseDto
    {
        public UserDto User { get; set; } = null!;
        public string AccessToken { get; set; } = null!;
        public string RefreshToken { get; set; } = null!;
        public DateTime ExpiresAt { get; set; }
    }

    public class RefreshTokenRequestDto
    {
        [Required]
        public string RefreshToken { get; set; } = null!;
    }
} 