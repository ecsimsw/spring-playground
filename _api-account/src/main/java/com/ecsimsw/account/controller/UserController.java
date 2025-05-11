package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.UpdatePasswordRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.account.service.UserService;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.account.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/account/signup")
    public ApiResponse<Long> user(@RequestBody SignUpRequest request) {
        log.info("Create user {}", request.username());
        var id = userService.create(request);
        return ApiResponse.success(id);
    }

    @GetMapping("/api/account/me")
    public ApiResponse<UserInfoResponse> me(AuthUser user) {
        var result = userService.userInfo(user.username());
        return ApiResponse.success(result);
    }

    @GetMapping("/api/account/roles")
    public ApiResponse<String[]> roles(AuthUser user) {
        return ApiResponse.success(user.roles());
    }

    @PutMapping("/api/account/password")
    public ApiResponse<Void> password(AuthUser user, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(user.username(), request.password());
        return ApiResponse.success();
    }

    @DeleteMapping("/api/account")
    public ApiResponse<Void> delete(AuthUser user) {
        userService.delete(user.username());
        return ApiResponse.success();
    }
}
