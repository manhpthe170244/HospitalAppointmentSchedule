using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Infrastructure.Data;
using HospitalAppointmentShedule.Infrastructure.Repository;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Infrastructure.UnitOfWork
{
    public class UnitOfWork : IUnitOfWork
    {
        private readonly AppointmentSchedulingDbContext _context;
        private readonly Dictionary<Type, object> _repositories;
        private bool _disposed;

        public IUserRepository Users { get; private set; }
        public IDoctorRepository Doctors { get; private set; }
        public IServiceRepository Services { get; private set; }
        public IReservationRepository Reservations { get; private set; }

        public UnitOfWork(AppointmentSchedulingDbContext context)
        {
            _context = context;
            _repositories = new Dictionary<Type, object>();

            // Initialize repositories
            Users = new UserRepository(_context);
            Doctors = new DoctorRepository(_context);
            Services = new ServiceRepository(_context);
            Reservations = new ReservationRepository(_context);
        }

        public IGenericRepository<T> Repository<T>() where T : class
        {
            var type = typeof(T);

            if (!_repositories.ContainsKey(type))
            {
                _repositories[type] = new GenericRepository<T>(_context);
            }

            return (IGenericRepository<T>)_repositories[type];
        }

        public async Task<int> SaveChangesAsync()
        {
            return await _context.SaveChangesAsync();
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (!_disposed)
            {
                if (disposing)
                {
                    _context.Dispose();
                }
                _disposed = true;
            }
        }
    }
} 