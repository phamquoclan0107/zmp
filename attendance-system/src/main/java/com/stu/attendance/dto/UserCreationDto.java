package com.stu.attendance.dto;

import com.stu.attendance.entity.TaiKhoan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDto {

    // Common identifier for both tai_khoan and nguoi_dung
    @NotBlank(message = "Mã người dùng không được để trống")
    private String maNguoiDung;

    // User information (nguoi_dung)
    @NotBlank(message = "Tên người dùng không được để trống")
    private String tenNguoiDung;

    private String maLop;

    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải đúng định dạng (10 chữ số)")
    private String sdt;

    @Email(message = "Email phải đúng định dạng")
    private String email;

    // Account information (tai_khoan)
//    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau = this.maNguoiDung;

//    @NotNull(message = "Vai trò không được để trống")
    private TaiKhoan.Role role;

    // Method to create TaiKhoan entity from DTO
    public TaiKhoan toTaiKhoan() {
        return new TaiKhoan(maNguoiDung, maNguoiDung, matKhau, role, null);
    }
}