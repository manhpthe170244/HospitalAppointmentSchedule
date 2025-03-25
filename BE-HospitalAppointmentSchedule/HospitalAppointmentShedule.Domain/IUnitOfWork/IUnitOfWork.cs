using HospitalAppointmentShedule.Domain.IRepository;
using System;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Domain.IUnitOfWork
{
    public interface IUnitOfWork : IDisposable
    {
        IUserRepository Users { get; }
        IDoctorRepository Doctors { get; }
        IServiceRepository Services { get; }
         IPatientRepository Patients { get; }
        IReservationRepository Reservations { get; }
        IGenericRepository<T> Repository<T>() where T : class;
        Task<int> SaveChangesAsync();
    }
} 