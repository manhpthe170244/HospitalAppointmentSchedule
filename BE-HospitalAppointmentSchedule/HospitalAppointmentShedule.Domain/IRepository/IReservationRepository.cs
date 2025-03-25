using HospitalAppointmentShedule.Domain.Models;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IReservationRepository : IGenericRepository<Reservation>
    {
        Task<Reservation?> GetReservationWithDetailsAsync(int reservationId);
        Task<IEnumerable<Reservation>> GetReservationsByPatientAsync(int patientId);
        Task<IEnumerable<Reservation>> GetReservationsByDateRangeAsync(DateTime startDate, DateTime endDate);
        Task<IEnumerable<Reservation>> GetReservationsByStatusAsync(string status);
        Task<bool> IsSlotAvailableAsync(int doctorScheduleId, DateTime appointmentDate);
        Task<IEnumerable<Reservation>> GetReservationsBySpecialtyAsync(int specialtyId, DateTime? date = null);
    }
} 