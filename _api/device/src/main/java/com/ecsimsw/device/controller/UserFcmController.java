package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.device.dto.FcmTokenRequest;
import com.ecsimsw.device.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserFcmController {

    private final FcmService fcmService;

    @GetMapping("/api/device/notification/fcm/token")
    public ApiResponse<List<String>> search(AuthUser authUser) {
        var tokens = fcmService.findAll(authUser.username());
        return ApiResponse.success(tokens);
    }

    @PostMapping("/api/device/notification/fcm/token")
    public ApiResponse<Void> upload(AuthUser authUser, @RequestBody FcmTokenRequest request) {
        fcmService.add(authUser.username(), request.token());
        return ApiResponse.success();
    }

    @DeleteMapping("/api/device/notification/fcm/token")
    public ApiResponse<Void> delete(AuthUser authUser, @RequestBody FcmTokenRequest request) {
        fcmService.remove(authUser.username(), request.token());
        return ApiResponse.success();
    }
}
