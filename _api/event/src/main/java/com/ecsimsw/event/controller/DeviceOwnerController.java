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
import java.util.List;

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
        var devices = new ArrayList<DeviceListResponse>();
        var tyDevices = tyApiService.getDeviceListByUsername(username);
        var akDevices = List.of(
            new DeviceListResponse("akf26e59904fd1", "Main entrance", "e16c186b2e2b", true, new ArrayList<>()),
            new DeviceListResponse("akf26e59904fd2", "Sub1 entrance", "e16c186b2e2b", true, new ArrayList<>()),
            new DeviceListResponse("akf26e59904fd3", "Sub2 entrance", "e16c186b2e2b", true, new ArrayList<>())
        );
        devices.addAll(tyDevices);
        devices.addAll(akDevices);
        deviceOwnerService.deleteAndSaveAll(username, devices);
    }
}
