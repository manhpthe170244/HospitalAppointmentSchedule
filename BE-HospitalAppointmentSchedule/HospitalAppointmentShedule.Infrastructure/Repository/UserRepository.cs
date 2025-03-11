using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Server;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class UserRepository : IUserRepository
    {
        
        private readonly AppointmentSchedulingDbContext _context;
        public UserRepository(AppointmentSchedulingDbContext context) { _context = context; }
        public async Task<User> GetUserByIdAsync(int id) => await _context.Users.FindAsync(id);
        public async Task<User> GetUserByEmailAsync(string email) => await _context.Users.FirstOrDefaultAsync(u => u.Email == email);
        public async Task AddUserAsync(User user) { await _context.Users.AddAsync(user); await _context.SaveChangesAsync(); }
        public async Task UpdateUserAsync(User user) { _context.Users.Update(user); await _context.SaveChangesAsync(); }
        public async Task ChangePasswordAsync(int userId, string oldPassword, string newPassword)
        {
            var user = await _context.Users.FindAsync(userId);
            if (user != null)
            {
                user.PasswordHash = newPassword; 
                _context.Users.Update(user);
                await _context.SaveChangesAsync();
            }
        }
    }
}
