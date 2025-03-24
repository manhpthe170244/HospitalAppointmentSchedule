using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.DBContext;
using HospitalAppointmentShedule.Infrastructure.Repository;

namespace HospitalAppointmentShedule.Infrastructure.UnitOfWork
{
    public class UnitOfWork : IUnitOfWork
    {
        private readonly AppointmentSchedulingDbContext _context;
        private IServiceRepository _serviceRepository;
        private IAppointmentRepository _appointmentRepository;

        public UnitOfWork(AppointmentSchedulingDbContext context)
        {
            _context = context;
        }

        public IServiceRepository Services
        {
            get
            {
                if (_serviceRepository == null)
                {
                    _serviceRepository = new ServiceRepository(_context);
                }
                return _serviceRepository;
            }
        }

        public IAppointmentRepository Appointments
        {
            get
            {
                if (_appointmentRepository == null)
                {
                    _appointmentRepository = new AppointmentRepository(_context);
                }
                return _appointmentRepository;
            }
        }

        public async Task<int> SaveChangesAsync()
        {
            return await _context.SaveChangesAsync();
        }

        public void Dispose()
        {
            _context.Dispose();
        }
    }
}