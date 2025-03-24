using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Domain.Models;

public partial class Patient
{
    public int PatientId { get; set; }

    public int? GuardianId { get; set; }

    public string? Rank { get; set; }

    public virtual User? Guardian { get; set; }

    public virtual User PatientNavigation { get; set; } = null!;

    public virtual ICollection<Reservation> Reservations { get; set; } = new List<Reservation>();
}
