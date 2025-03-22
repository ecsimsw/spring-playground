package com.ecsimsw.account.controller;

import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.dto.SignUpResponse;
import com.ecsimsw.account.dto.UpdatePasswordRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.account.service.UserService;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/user/signup")
    public ApiResponse<SignUpResponse> user(@RequestBody SignUpRequest request) {
        var id = userService.create(request);
//        emailService.outbox(
//            user.getEmail(),
//            EmailType.SIGN_UP,
//            user.getUsername()
//        );
        return ApiResponse.success(new SignUpResponse(id));
    }

    @GetMapping("/api/user/me")
    public ApiResponse<UserInfoResponse> me(AuthUser user) {
        var result = userService.userInfo(user.username());
        return ApiResponse.success(result);
    }

    @GetMapping("/api/user/roles")
    public ApiResponse<String[]> roles(AuthUser user) {
        return ApiResponse.success(user.roles());
    }

    @PutMapping("/api/user/password")
    public ApiResponse<Void> password(AuthUser user, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(user.username(), request.password());
        return ApiResponse.success();
    }

    @DeleteMapping("/api/user")
    public ApiResponse<Void> delete(AuthUser user) {
        userService.delete(user.username());
        return ApiResponse.success();
    }
}
