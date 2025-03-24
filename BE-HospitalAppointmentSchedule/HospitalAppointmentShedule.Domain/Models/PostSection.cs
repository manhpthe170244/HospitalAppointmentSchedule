using System;
using System.Collections.Generic;

namespace HospitalAppointmentShedule.Domain.Models;

public partial class PostSection
{
    public int SectionId { get; set; }

    public int PostId { get; set; }

    public string SectionTitle { get; set; } = null!;

    public string SectionContent { get; set; } = null!;

    public int SectionIndex { get; set; }

    public string? PostImageUrl { get; set; }

    public virtual Post Post { get; set; } = null!;
}
