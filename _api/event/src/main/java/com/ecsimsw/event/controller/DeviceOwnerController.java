package com.ecsimsw.event.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.event.service.DeviceOwnerService;
import com.ecsimsw.sdkcommon.dto.api.DeviceListResponse;
import com.ecsimsw.sdkty.service.TyApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceOwnerController {

    private final TyApiService tyApiService;
    private final DeviceOwnerService deviceOwnerService;

    @InternalHandler
    @PostMapping("/api/event/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        bindTestDevices(username);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

    private void bindTestDevices(String username) {
        var devices = new ArrayList<>(tyApiService.getDeviceListByUsername(username));
        var akTestDevice = new DeviceListResponse(
            "akf26e59904fd2",
            "Main entrance",
            "e16c186b2e2b",
            true,
            new ArrayList<>()
        );
        devices.add(akTestDevice);
        deviceOwnerService.deleteAndSaveAll(username, devices);
    };
}
