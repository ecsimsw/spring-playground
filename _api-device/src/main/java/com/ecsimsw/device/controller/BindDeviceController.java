package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
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
public class BindDeviceController {

    private final ExternalPlatformService externalPlatformService;
    private final DeviceService deviceService;

    @InternalHandler
    @PostMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var deviceInfos = externalPlatformService.getDeviceList(username);
        deviceService.refresh(username, deviceInfos);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

    @GetMapping("/api/device/list")
    public ApiResponse<List<DeviceInfoResponse>> list(AuthUser authUser) {
        var result = deviceService.deviceList(authUser.username());
        return ApiResponse.success(result);
    }

    @PostMapping("/api/device/{deviceId}")
    public ApiResponse<Void> control(
        AuthUser authUser,
        @PathVariable String deviceId,
        @RequestBody List<DeviceStatus> deviceStatuses
    ) {
        deviceService.checkDeviceOwner(authUser.username(), deviceId);
        externalPlatformService.command(deviceId, deviceStatuses);
        return ApiResponse.success();
    }
}
