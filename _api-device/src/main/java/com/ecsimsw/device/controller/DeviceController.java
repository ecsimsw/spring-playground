package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.springsdkexternalplatform.dto.DevicesResponse;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DeviceController {

    private final DeviceService deviceService;
    private final ExternalPlatformService externalPlatformService;

    @GetMapping("/api/device/me")
    public ApiResponse<List<String>> devices(AuthUser authUser) {
        var deviceList = externalPlatformService.getDeviceList(authUser.uid());
        deviceService.updateDeviceList(deviceList);
        deviceService.getDeviceIds()
    }
}
