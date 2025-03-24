using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.Models;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IAppointmentRepository
    {
        Task<IEnumerable<Reservation>> GetUserAppointmentsAsync(int userId);
        Task<Reservation> GetAppointmentByIdAsync(int reservationId);
        Task AddAppointmentAsync(Reservation reservation);
        Task CancelAppointmentAsync(int reservationId, string cancellationReason);
        Task UpdateAppointmentAsync(Reservation reservation);
        Task<IEnumerable<Reservation>> GetAppointmentsByStatusAsync(string status);
    }
}
