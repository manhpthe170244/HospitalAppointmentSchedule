using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Domain.Models;
using HospitalAppointmentShedule.Infrastructure.Data;
using HospitalAppointmentShedule.Infrastructure.Repository;

namespace HospitalAppointmentShedule.Domain.Repository
{
    public class PatientRepository : GenericRepository<Patient>, IPatientRepository
    {
        public PatientRepository(AppointmentSchedulingDbContext context) : base(context)
        {
        }

        // Implement any specific methods for Patient if needed
    }
}