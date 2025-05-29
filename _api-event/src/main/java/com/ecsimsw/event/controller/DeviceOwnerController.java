package com.ecsimsw.event.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.event.service.DeviceOwnerService;
import com.ecsimsw.springsdkexternalplatform.service.ExternalPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceOwnerController {

    private final ExternalPlatformService externalPlatformService;
    private final DeviceOwnerService deviceOwnerService;

    @InternalHandler
    @PostMapping("/api/event/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var deviceInfos = externalPlatformService.getDeviceList(username);
        deviceOwnerService.refresh(username, deviceInfos);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

}
