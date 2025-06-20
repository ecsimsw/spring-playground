package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.dto.DeviceStatusValue;
import com.ecsimsw.common.service.TbApiService;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceService;
import com.ecsimsw.device.service.RpcService;
import com.ecsimsw.sdkty.service.TyApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BindDeviceController {

    private final TyApiService tyApiService;
    private final TbApiService tbApiService;
    private final DeviceService deviceService;
    private final RpcService rpcService;

    @InternalHandler
    @PostMapping("/api/device/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var tyDeviceInfos = tyApiService.getDeviceList(username);
        deviceService.deleteAndSaveAll(username, tyDeviceInfos);
        tyDeviceInfos.forEach(
            ttyDevice -> {
                tbApiService.updateCredential(ttyDevice.getId());
                rpcService.connect(ttyDevice.getId());
            }
        );
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
        @RequestBody List<DeviceStatusValue> deviceStatusValues
    ) {
        var bindDevice = deviceService.getUserDevice(authUser.username(), deviceId);
        tyApiService.command(deviceId, bindDevice.productId(), deviceStatusValues);
        return ApiResponse.success();
    }
}
