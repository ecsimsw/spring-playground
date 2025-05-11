package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.UserInfoAdminResponse;
import com.ecsimsw.account.service.AdminService;
import com.ecsimsw.common.domain.RoleType;
import com.ecsimsw.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/api/account/admin/users")
    public ApiResponse<Page<UserInfoAdminResponse>> users(Pageable pageable) {
        var response = adminService.users(pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/api/account/admin/role-names")
    public ApiResponse<RoleType[]> roles() {
        var roleNames = RoleType.values();
        return ApiResponse.success(roleNames);
    }

    @DeleteMapping("/api/account/admin/users/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ApiResponse.success();
    }
}
