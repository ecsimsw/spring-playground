package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.dto.PairingRequest;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceStatus;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceController {

    private final ExternalPlatformService externalPlatformService;
    private final DeviceService deviceService;

    @InternalHandler
    @PostMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        log.info("Refresh : {}", username);
        var deviceInfos = externalPlatformService.getDeviceList(username);
        deviceService.refresh(username, deviceInfos);
        return ApiResponse.success();
    }

    @PostMapping("/api/device/beta/pairing")
    public ApiResponse<Void> pairing(
        AuthUser authUser,
        @RequestBody PairingRequest pairingRequest
    ) {
        var deviceResult = externalPlatformService.deviceInfo(pairingRequest.deviceId());
        deviceService.bindDevices(authUser.username(), List.of(deviceResult));
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
