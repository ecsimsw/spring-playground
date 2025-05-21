package com.ecsimsw.notification.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.notification.dto.FcmTokenRequest;
import com.ecsimsw.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final FcmService fcmService;

    @GetMapping("/api/notification/fcm")
    public ApiResponse<List<String>> search(AuthUser authUser) {
        var tokens = fcmService.findAll(authUser.username());
        return ApiResponse.success(tokens);
    }

    @PostMapping("/api/notification/fcm")
    public ApiResponse<Void> upload(AuthUser authUser, @RequestBody FcmTokenRequest request) {
        fcmService.add(authUser.username(), request.token());
        return ApiResponse.success();
    }

    @DeleteMapping("/api/notification/fcm")
    public ApiResponse<Void> delete(AuthUser authUser, @RequestBody FcmTokenRequest request) {
        fcmService.remove(authUser.username(), request.token());
        return ApiResponse.success();
    }

    @GetMapping("/api/notification/up")
    public ApiResponse<Void> up() {
        return ApiResponse.success();
    }
}
