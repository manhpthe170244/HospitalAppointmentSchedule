﻿using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Domain.Models;

public partial class Room
{
    public int RoomId { get; set; }

    public string RoomName { get; set; } = null!;

    public string RoomType { get; set; } = null!;

    public string Location { get; set; } = null!;

    public virtual ICollection<DoctorSchedule> DoctorSchedules { get; set; } = new List<DoctorSchedule>();
}
