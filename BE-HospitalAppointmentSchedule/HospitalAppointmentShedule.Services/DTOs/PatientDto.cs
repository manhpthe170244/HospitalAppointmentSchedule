public class PatientDto
{
    public int PatientId { get; set; }
    public string UserName { get; set; } = null!;
    public string Email { get; set; } = null!;
    public string Phone { get; set; } = null!;
    public DateTime? DateOfBirth { get; set; }
    public string? Gender { get; set; }
    public string? Address { get; set; }
    public string? GuardianName { get; set; }
    public bool IsVerify { get; set; } // Thêm thuộc tính này
}