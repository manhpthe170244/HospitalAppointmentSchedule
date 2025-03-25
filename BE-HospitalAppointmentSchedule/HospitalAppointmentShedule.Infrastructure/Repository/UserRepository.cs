using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class UserRepository : GenericRepository<User>, IUserRepository
    {
        public UserRepository(AppointmentSchedulingDbContext context) : base(context)
        {
        }

        public async Task<User?> GetByEmailAsync(string email)
        {
            return await _dbSet.FirstOrDefaultAsync(u => u.Email == email);
        }

        public async Task<User?> GetByPhoneAsync(string phone)
        {
            return await _dbSet.FirstOrDefaultAsync(u => u.Phone == phone);
        }

        public async Task<User?> GetUserWithRolesAsync(int userId)
        {
            return await _dbSet
                .Include(u => u.Roles)
                .FirstOrDefaultAsync(u => u.UserId == userId);
        }

        public async Task<bool> IsEmailUniqueAsync(string email, int? excludeUserId = null)
        {
            if (excludeUserId.HasValue)
            {
                return !await _dbSet.AnyAsync(u => u.Email == email && u.UserId != excludeUserId);
            }
            return !await _dbSet.AnyAsync(u => u.Email == email);
        }

        public async Task<bool> IsPhoneUniqueAsync(string phone, int? excludeUserId = null)
        {
            if (excludeUserId.HasValue)
            {
                return !await _dbSet.AnyAsync(u => u.Phone == phone && u.UserId != excludeUserId);
            }
            return !await _dbSet.AnyAsync(u => u.Phone == phone);
        }
    }
} 