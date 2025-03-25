using HospitalAppointmentShedule.Domain.Models;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IUserRepository : IGenericRepository<User>
    {
        Task<User?> GetByEmailAsync(string email);
        Task<User?> GetByPhoneAsync(string phone);
        Task<User?> GetUserWithRolesAsync(int userId);
        Task<bool> IsEmailUniqueAsync(string email, int? excludeUserId = null);
        Task<bool> IsPhoneUniqueAsync(string phone, int? excludeUserId = null);
    }
} 