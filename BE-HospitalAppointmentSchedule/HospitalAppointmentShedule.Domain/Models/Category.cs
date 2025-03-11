﻿using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Server;

public partial class Category
{
    public int CategoryId { get; set; }

    public string CategoryName { get; set; } = null!;

    public string Image { get; set; } = null!;

    public virtual ICollection<Service> Services { get; set; } = new List<Service>();
}
