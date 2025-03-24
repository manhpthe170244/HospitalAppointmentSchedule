using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Domain.Models;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IFeedbackRepository
    {
        Task<IEnumerable<Feedback>> GetAllAsync();
        Task<Feedback> GetByIdAsync(int id);
        Task<Feedback> AddAsync(Feedback feedback);
        Task UpdateAsync(Feedback feedback);
        Task DeleteAsync(int id);
        Task<IEnumerable<Feedback>> GetFeedbacksByReservationIdAsync(int reservationId);
        Task<IEnumerable<Feedback>> GetFeedbacksByDoctorIdAsync(int doctorId);
    }
}
