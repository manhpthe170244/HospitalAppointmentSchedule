using HospitalAppointmentShedule.Domain.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IDoctorRepository : IGenericRepository<Doctor>
    {
        Task<Doctor?> GetDoctorWithDetailsAsync(int doctorId);
        Task<IEnumerable<Doctor>> GetDoctorsBySpecialtyAsync(int specialtyId);
        Task<IEnumerable<Doctor>> GetDoctorsByServiceAsync(int serviceId);
        Task<IEnumerable<DoctorSchedule>> GetDoctorSchedulesAsync(int doctorId, DateTime? date = null);
        Task<IEnumerable<Reservation>> GetDoctorReservationsAsync(int doctorId, DateTime? date = null);
    }
} 