package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DeviceController {

    private final ExternalPlatformService externalPlatformService;
    private final DeviceService deviceService;

    //    @InternalHandler
    @GetMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var userId = externalPlatformService.getUserIdByUsername(username);
        var deviceResults = externalPlatformService.getDeviceList(userId);
        deviceService.refresh(username, deviceResults);
        return ApiResponse.success();
    }

    @GetMapping("/api/device/list")
    public ApiResponse<List<DeviceInfoResponse>> list(AuthUser authUser) {
        var result = deviceService.deviceList(authUser.username());
        return ApiResponse.success(result);
    }

    @GetMapping("/api/device/{deviceId}")
    public ApiResponse<DeviceInfoResponse> status(@PathVariable String deviceId) {
        var result = deviceService.status(deviceId);
        return ApiResponse.success(result);
    }

    @PostMapping("/api/device/{deviceId}")
    public ApiResponse<Void> control(
        @PathVariable String deviceId,
        @RequestBody List<DeviceStatus> deviceStatuses
    ) {
        externalPlatformService.command(deviceId, deviceStatuses);
        return ApiResponse.success();
    }
}
