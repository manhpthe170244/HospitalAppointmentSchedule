﻿using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Server;

public partial class Device
{
    public int DeviceId { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public string? Functionality { get; set; }

    public virtual ICollection<Service> Services { get; set; } = new List<Service>();

    public virtual ICollection<Specialty> Specialties { get; set; } = new List<Specialty>();
}
