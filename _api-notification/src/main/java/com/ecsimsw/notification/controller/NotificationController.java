package com.ecsimsw.notification.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.notification.dto.FcmUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotificationController {

    @PostMapping("/api/notification/fcm")
    public ApiResponse<Void> fcm(@RequestBody FcmUpdateRequest updateRequest) {
        return ApiResponse.success();
    }

    @GetMapping("/api/notification/up")
    public ApiResponse<Void> up() {
        return ApiResponse.success();
    }
}
