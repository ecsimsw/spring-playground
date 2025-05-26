package com.ecsimsw.notification.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.notification.dto.FcmMessageRequest;
import com.ecsimsw.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GlobalController {

    @Value("${VERSION:unknown}")
    private String version;

    private final FcmService fcmService;

    @PostMapping("/api/notification/beta/fcm")
    public ApiResponse<Void> sendMessage(@RequestBody FcmMessageRequest fcmMessageRequest) {
        fcmService.sendMessage(
            fcmMessageRequest.targetToken(),
            fcmMessageRequest.title(),
            fcmMessageRequest.body()
        );
        return ApiResponse.success();
    }

    @GetMapping("/api/notification/up")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(version);
    }
}
