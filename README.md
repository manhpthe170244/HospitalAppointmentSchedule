# Hệ Thống Đặt Lịch Khám Cho Bệnh Viện  

## Sử dụng Repository Pattern và UnitOfWork cho Backend để triển khai các API  

### Cấu trúc hệ thống  

- **Domain**: Chứa các Entity của Database và các interface của Repository, UnitOfWork.  
- **Infrastructure**: Chứa `DbContext` và các Repository, UnitOfWork kế thừa từ các interface của tầng Domain.  
- **Service**: Dùng để xử lý nghiệp vụ mà các Repository chưa xử lý được, vì Repository chỉ thực hiện CRUD với database.  

### Ví dụ trong `UserService`  

```csharp
public class UserService : IUserService {
    private readonly IUserRepository _userRepository;
    
    public UserService(IUserRepository userRepository) {
        _userRepository = userRepository;
    }

    public async Task<User> GetUserByIdAsync(int id) {
        var user = await _userRepository.GetUserByIdAsync(id);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }
}
```

### Interface `IUserRepository`  

```csharp
public interface IUserRepository
{
    Task<User> GetUserByIdAsync(int id);
    Task<User> GetUserByEmailAsync(string email);
    Task AddUserAsync(User user);
    Task UpdateUserAsync(User user);
    Task ChangePasswordAsync(int userId, string oldPassword, string newPassword);
}
```

### Repository `UserRepository`  

```csharp
public class UserRepository : IUserRepository
{
    private readonly AppointmentSchedulingDbContext _context;
    
    public UserRepository(AppointmentSchedulingDbContext context) 
    { 
        _context = context; 
    }

    public async Task<User> GetUserByIdAsync(int id) => await _context.Users.FindAsync(id);

    public async Task<User> GetUserByEmailAsync(string email) => 
        await _context.Users.FirstOrDefaultAsync(u => u.Email == email);

    public async Task AddUserAsync(User user) 
    { 
        await _context.Users.AddAsync(user); 
        await _context.SaveChangesAsync(); 
    }

    public async Task UpdateUserAsync(User user) 
    { 
        _context.Users.Update(user); 
        await _context.SaveChangesAsync(); 
    }

    public async Task ChangePasswordAsync(int userId, string oldPassword, string newPassword)
    {
        var user = await _context.Users.FindAsync(userId);
        if (user != null)
        {
            user.PasswordHash = newPassword; 
            _context.Users.Update(user);
            await _context.SaveChangesAsync();
        }
    }
}
```
Ta thấy ở UserRepository kế thừa lại interface và làm công việc CRUD với database và UserService sẽ gọi lại UserRepository hàm GetUserByIdAsync và thêm nghiệp vụ check user = null việc mà UserRepository không check 
### Lý do cần có Service thay vì chỉ dùng Repository  

1. **Tái sử dụng logic nghiệp vụ**  
   - Service có thể kết hợp nhiều Repository khác nhau để thực hiện các quy trình phức tạp.  
   - Ví dụ: Khi lấy thông tin người dùng trong `UserService`, có thể cần thêm dữ liệu từ `ReservationRepository` hoặc `FeedbackRepository`.  

2. **Tổ chức mã nguồn rõ ràng**  
   - Repository chỉ nên tập trung vào truy vấn dữ liệu từ cơ sở dữ liệu.  
   - Service có thể chứa logic kiểm tra, xử lý dữ liệu trước khi trả về API.  

3. **Dễ mở rộng và bảo trì**  
   - Nếu sau này cần thêm một bước kiểm tra hoặc thay đổi cách dữ liệu được xử lý trước khi trả về, chỉ cần sửa trong Service mà không phải thay đổi Repository hoặc Controller.  

4. **Giảm phụ thuộc trực tiếp giữa Controller và Repository**  
   - Controller chỉ giao tiếp với Service, không làm việc trực tiếp với Repository, giúp cho code dễ kiểm soát hơn.  
