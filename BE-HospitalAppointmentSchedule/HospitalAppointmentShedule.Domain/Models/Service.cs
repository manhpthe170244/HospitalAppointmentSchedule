﻿using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Domain.Models;
public partial class Service
{
    public int ServiceId { get; set; }

    public string ServiceName { get; set; } = null!;

    public string? Overview { get; set; }

    public string? Process { get; set; }

    public string? TreatmentTechniques { get; set; }

    public decimal Price { get; set; }

    public TimeOnly? EstimatedTime { get; set; }

    public bool? IsPrepayment { get; set; }

    public int? ParentServiceId { get; set; }

    public int SpecialtyId { get; set; }

    public string? Image { get; set; }

    public virtual ICollection<DoctorSchedule> DoctorSchedules { get; set; } = new List<DoctorSchedule>();

    public virtual ICollection<Service> InverseParentService { get; set; } = new List<Service>();

    public virtual Service? ParentService { get; set; }

    public virtual Specialty Specialty { get; set; } = null!;

    public virtual ICollection<Device> Devices { get; set; } = new List<Device>();

    public virtual ICollection<Doctor> Doctors { get; set; } = new List<Doctor>();
}
