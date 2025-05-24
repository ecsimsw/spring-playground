package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    @InternalHandler
    @GetMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        deviceService.refresh(username);
        return ApiResponse.success();
    }

    @PostMapping("/api/device/{deviceId}")
    public ApiResponse<List<String>> control(@PathVariable String deviceId) {
        return null;
    }

    @GetMapping("/api/device/{deviceId}")
    public ApiResponse<List<String>> status(@PathVariable String deviceId) {
        return null;
    }
}
