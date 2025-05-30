package com.stu.attendance.service;

import com.stu.attendance.dto.UserInfoDto;
import com.stu.attendance.entity.NguoiDung;
import com.stu.attendance.entity.TaiKhoan;
import com.stu.attendance.exception.ResourceNotFoundException;
import com.stu.attendance.repository.UserRepository;
import com.stu.attendance.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;


    public UserInfoDto getCurrentUserProfile() {
        String currentUsername = securityUtils.getCurrentUsername();
        NguoiDung nguoiDung = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng hiện tại"));
        return convertToDto(nguoiDung);
    }


    public UserInfoDto getUserById(String userId) {
        NguoiDung nguoiDung = userRepository.findByMaNguoiDung(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng có mã: " + userId));
        return convertToDto(nguoiDung);
    }


    @Transactional
    public UserInfoDto updateUserProfile(UserInfoDto userInfoDto) {
        String currentUsername = securityUtils.getCurrentUsername();
        NguoiDung currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng hiện tại"));

        // Check if the user is trying to update their own profile or is an admin
        if (!currentUser.getMaNguoiDung().equals(userInfoDto.getUserId()) &&
                currentUser.getTaiKhoan().getRole() != TaiKhoan.Role.admin) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật thông tin của người dùng khác");
        }

        // Get the user to update
        NguoiDung userToUpdate = userInfoDto.getUserId().equals(currentUser.getMaNguoiDung())
                ? currentUser
                : userRepository.findByMaNguoiDung(userInfoDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng có mã: " + userInfoDto.getUserId()));

        // Update fields (only those that are supposed to be updatable)
        if (userInfoDto.getFullName() != null) {
            userToUpdate.setTenNguoiDung(userInfoDto.getFullName());
        }

        if (userInfoDto.getPhone() != null) {
            userToUpdate.setSdt(userInfoDto.getPhone());
        }

        if (userInfoDto.getEmail() != null && !userInfoDto.getEmail().equals(userToUpdate.getEmail())) {
            // Check if email is already in use
            if (userRepository.existsByEmail(userInfoDto.getEmail())) {
                throw new IllegalArgumentException("Email đã được sử dụng bởi người dùng khác");
            }
            userToUpdate.setEmail(userInfoDto.getEmail());
        }

        // Save the updated user
        NguoiDung savedUser = userRepository.save(userToUpdate);
        return convertToDto(savedUser);
    }


    @Transactional
    public void updateNotificationSettings(boolean emailNotifications, boolean zaloNotifications) {
        // Note: This is a placeholder since notification settings are not in the entity model
        // In a real implementation, you would need to extend the NguoiDung entity to include notification preferences
        String currentUsername = securityUtils.getCurrentUsername();
        NguoiDung currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng hiện tại"));

        // Here you would update the notification settings
        // Since these fields don't exist in the current entity, this is just a placeholder
        // currentUser.setEmailNotificationsEnabled(emailNotifications);
        // currentUser.setZaloNotificationsEnabled(zaloNotifications);

        userRepository.save(currentUser);
    }


    @Transactional
    public void linkZaloAccount(String zaloId) {
        // Note: This is a placeholder since Zalo account linking is not in the entity model
        // In a real implementation, you would need to extend the NguoiDung entity to include Zalo ID
        String currentUsername = securityUtils.getCurrentUsername();
        NguoiDung currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin người dùng hiện tại"));

        // Here you would link the Zalo account
        // Since this field doesn't exist in the current entity, this is just a placeholder
        // currentUser.setZaloId(zaloId);

        userRepository.save(currentUser);
    }


    public List<UserInfoDto> findAllUsers(String keyword, TaiKhoan.Role role) {
        List<NguoiDung> users = userRepository.findAllUsers(keyword, role);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public UserInfoDto convertToDto(NguoiDung nguoiDung) {
        if (nguoiDung == null) {
            return null;
        }

        UserInfoDto dto = new UserInfoDto();
        dto.setUserId(nguoiDung.getMaNguoiDung());
        dto.setFullName(nguoiDung.getTenNguoiDung());
        dto.setEmail(nguoiDung.getEmail());
        dto.setPhone(nguoiDung.getSdt());

        if (nguoiDung.getLopSinhVien() != null) {
            dto.setClassName(nguoiDung.getLopSinhVien().getTenLop());
            dto.setClassId(nguoiDung.getLopSinhVien().getMaLop());
        }

        if (nguoiDung.getTaiKhoan() != null) {
            dto.setRole(nguoiDung.getTaiKhoan().getRole());
            dto.setUsername(nguoiDung.getTaiKhoan().getTenTaiKhoan());
        }

        return dto;
    }
}