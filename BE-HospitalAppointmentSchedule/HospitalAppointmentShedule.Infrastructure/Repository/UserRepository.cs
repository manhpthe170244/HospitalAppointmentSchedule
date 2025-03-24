using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.DBContext;
using Microsoft.EntityFrameworkCore;

namespace HospitalAppointmentShedule.Infrastructure.Repository
{
    public class UserRepository : IUserRepository
    {
        private readonly AppointmentSchedulingDbContext _context;

        public UserRepository(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<User>> GetAllAsync()
        {
            return await _context.Users
                .Include(u => u.Roles)
                .Include(u => u.Doctor)
                .Include(u => u.PatientPatientNavigation)
                .Include(u => u.Receptionist)
                .ToListAsync();
        }

        public async Task<User> GetByIdAsync(int id)
        {
            return await _context.Users
                .Include(u => u.Roles)
                .Include(u => u.Doctor)
                .Include(u => u.PatientPatientNavigation)
                .Include(u => u.Receptionist)
                .FirstOrDefaultAsync(u => u.UserId == id);
        }

        public async Task<User> GetByEmailAsync(string email)
        {
            return await _context.Users
                .Include(u => u.Roles)
                .Include(u => u.Doctor)
                .Include(u => u.PatientPatientNavigation)
                .Include(u => u.Receptionist)
                .FirstOrDefaultAsync(u => u.Email == email);
        }

        public async Task<User> GetByUsernameAsync(string username)
        {
            return await _context.Users
                .Include(u => u.Roles)
                .Include(u => u.Doctor)
                .Include(u => u.PatientPatientNavigation)
                .Include(u => u.Receptionist)
                .FirstOrDefaultAsync(u => u.UserName == username);
        }

        public async Task<User> AddAsync(User user)
        {
            await _context.Users.AddAsync(user);
            await _context.SaveChangesAsync();
            return user;
        }

        public async Task UpdateAsync(User user)
        {
            _context.Users.Update(user);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user != null)
            {
                _context.Users.Remove(user);
                await _context.SaveChangesAsync();
            }
        }

        public async Task<IEnumerable<User>> GetUsersByRoleAsync(string role)
        {
            return await _context.Users
                .Include(u => u.Roles)
                .Include(u => u.Doctor)
                .Include(u => u.PatientPatientNavigation)
                .Include(u => u.Receptionist)
                .Where(u => u.Roles.Any(r => r.RoleName == role))
                .ToListAsync();
        }

        public async Task<bool> ExistsAsync(string username, string email)
        {
            return await _context.Users.AnyAsync(u => u.UserName == username || u.Email == email);
        }
    }
}
