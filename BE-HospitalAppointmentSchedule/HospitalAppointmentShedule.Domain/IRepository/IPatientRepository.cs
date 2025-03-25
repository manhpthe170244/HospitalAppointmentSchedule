using HospitalAppointmentShedule.Domain.Models;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IPatientRepository : IGenericRepository<Patient>
    {
        // Add any specific methods for Patient if needed
    }
}