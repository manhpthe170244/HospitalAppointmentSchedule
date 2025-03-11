using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HospitalAppointmentShedule.Server;

namespace HospitalAppointmentShedule.Domain.IRepository
{
    public interface IFeedbackRepository
    {
        Task SubmitFeedbackAsync(Feedback feedback);
        Task<IEnumerable<Feedback>> GetDoctorFeedbackAsync(int doctorId);
/*        Task<IEnumerable<Feedback>> GetServiceFeedbackAsync(int serviceId);*/
    }
}
