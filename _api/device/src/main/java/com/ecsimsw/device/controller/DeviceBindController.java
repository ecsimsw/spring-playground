package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceBindService;
import com.ecsimsw.sdkty.service.TyApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceBindController {

    private final TyApiService tyApiService;
    private final DeviceBindService deviceBindService;

    @InternalHandler
    @PostMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var tyDeviceList = tyApiService.getDeviceListByUsername(username);
        deviceBindService.deleteAndSaveAll(username, tyDeviceList);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

    @GetMapping("/api/device/list")
    public ApiResponse<List<DeviceInfoResponse>> list(AuthUser authUser) {
        var result = deviceBindService.deviceList(authUser.username());
        return ApiResponse.success(result);
    }
}
