# Vision and Scope Document

## 1. Tầm nhìn dự án

Dự án Hospital Appointment Schedule nhằm phát triển một hệ thống đặt lịch khám bệnh hiện đại, giúp bệnh nhân dễ dàng đặt lịch hẹn với bác sĩ, quản lý thông tin khám bệnh và thanh toán. Hệ thống cung cấp giải pháp toàn diện cho quy trình khám chữa bệnh, từ việc chọn bác sĩ theo chuyên khoa đến hoàn thành thanh toán và nhận phản hồi sau khám.

## 2. Phạm vi dự án

```mermaid
%%{init: {"mindmap": {"padding": 20, "rankPadding": 30, "useMaxWidth": false}, "themeVariables": {"fontSize": "16px"}} }%%
mindmap
  root((Hệ thống<br>đặt lịch<br>khám bệnh))
    Quản lý người dùng
      Đăng ký/Đăng nhập
      Phân quyền
      Quản lý thông tin cá nhân
    Quản lý đặt lịch
      Tìm kiếm bác sĩ
      Xem lịch khám
      Đặt lịch hẹn
      Hủy/Đổi lịch hẹn
    Quản lý dịch vụ y tế
      Danh sách dịch vụ
      Danh sách chuyên khoa
      Đánh giá dịch vụ
    Quản lý thanh toán
      Thanh toán trực tuyến
      Lịch sử thanh toán
    Hồ sơ y tế
      Hồ sơ bệnh nhân
      Lịch sử khám bệnh
    Thống kê báo cáo
      Báo cáo cho quản trị viên
      Phân tích dữ liệu
```

## 3. Đối tượng người dùng

- **Bệnh nhân**: Đặt lịch khám, xem thông tin bác sĩ, thanh toán, quản lý lịch hẹn
- **Bác sĩ**: Quản lý lịch làm việc, xem danh sách bệnh nhân, cập nhật hồ sơ cá nhân
- **Lễ tân**: Xác nhận lịch hẹn, hỗ trợ bệnh nhân, quản lý lịch trình bệnh viện
- **Quản trị viên**: Quản lý người dùng, phân quyền, xem báo cáo thống kê 