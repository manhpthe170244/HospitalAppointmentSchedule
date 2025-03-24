using HospitalAppointmentShedule.Domain.Models;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IDoctorScheduleRepository
    {
        Task<IEnumerable<DoctorSchedule>> GetAllAsync();
        Task<DoctorSchedule> GetByIdAsync(int id);
        Task<IEnumerable<DoctorSchedule>> GetByDoctorIdAsync(int doctorId);
        Task<DoctorSchedule> AddAsync(DoctorSchedule schedule);
        Task UpdateAsync(DoctorSchedule schedule);
        Task DeleteAsync(int id);
    }
}