package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.UpdateUserRoleRequest;
import com.ecsimsw.account.dto.UserInfoAdminResponse;
import com.ecsimsw.account.service.AdminService;
//import com.ecsimsw.auth.service.AuthService;
import com.ecsimsw.common.domain.RoleType;
import com.ecsimsw.common.domain.UserStatus;
import com.ecsimsw.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {

//    private final AuthService authService;
    private final AdminService adminService;

    @GetMapping("/api/admin/users")
    public ApiResponse<Page<UserInfoAdminResponse>> users(UserStatus status, boolean deleted, Pageable pageable) {
        var response = adminService.users(status, deleted, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/api/admin/role-names")
    public ApiResponse<RoleType[]> roles() {
        var roleNames = RoleType.values();
        return ApiResponse.success(roleNames);
    }

    @PutMapping("/api/admin/users/{userId}/role")
    public ApiResponse<Void> updateUserRole(@PathVariable Long userId, @RequestBody List<UpdateUserRoleRequest> request) {
        adminService.updateUserRole(userId, request);
        return ApiResponse.success();
    }

    @PutMapping("/api/admin/users/{userId}/approve")
    public ApiResponse<Void> approve(@PathVariable Long userId) {
        adminService.approve(userId);
        return ApiResponse.success();
    }

    @PutMapping("/api/admin/users/{userId}/disapprove")
    public ApiResponse<Void> disapprove(@PathVariable Long userId) {
        adminService.disapprove(userId);
        return ApiResponse.success();
    }

    @DeleteMapping("/api/admin/users/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
//        authService.blockUser(userId);
//        SecurityContextHolder.getContext().setAuthentication(null);
        return ApiResponse.success();
    }
}
