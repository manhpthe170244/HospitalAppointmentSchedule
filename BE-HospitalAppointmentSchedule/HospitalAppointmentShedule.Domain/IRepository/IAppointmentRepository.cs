using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Server;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IAppointmentRepository
    {
        Task<IEnumerable<Reservation>> GetUserAppointmentsAsync(int userId);
        Task AddAppointmentAsync(Reservation reservation);
        Task CancelAppointmentAsync(int reservationId);
        Task UpdateAppointmentAsync(Reservation reservation);
    }
}
