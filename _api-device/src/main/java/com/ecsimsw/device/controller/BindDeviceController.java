package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.domain.Products;
import com.ecsimsw.springsdkexternalplatform.dto.DeviceInfo;
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

        // XXX :: Add dummies
        if (username.contains("0000")) {
            deviceService.bindDevices(username, List.of(
                new DeviceInfo("POWER2", "POWER2", Products.Power.productId, false, List.of(new DeviceStatus("switch", true))),
                new DeviceInfo("POWER8", "POWER8", Products.Power.productId, false, List.of(new DeviceStatus("switch", true))),
                new DeviceInfo("POWER11", "POWER4", Products.Power.productId, false, List.of(new DeviceStatus("switch", true))),
                new DeviceInfo("POWER13", "POWER13", Products.Power.productId, false, List.of(new DeviceStatus("switch", true)))
            ));
        }
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
        var bindDevice = deviceService.getUserDevice(authUser.username(), deviceId);
        externalPlatformService.command(deviceId, bindDevice.productId(), deviceStatuses);
        return ApiResponse.success();
    }
}
