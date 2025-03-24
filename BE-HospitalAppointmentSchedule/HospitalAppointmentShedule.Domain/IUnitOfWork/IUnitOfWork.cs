using HospitalAppointmentShedule.Domain.IRepository;

namespace HospitalAppointmentShedule.Domain.IUnitOfWork
{
    public interface IUnitOfWork : IDisposable
    {
        IServiceRepository Services { get; }
        IAppointmentRepository Appointments { get; }
        Task<int> SaveChangesAsync();
    }
}