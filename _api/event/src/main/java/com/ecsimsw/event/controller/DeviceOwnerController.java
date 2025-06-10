package com.ecsimsw.event.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.event.service.DeviceOwnerService;
import com.ecsimsw.sdkty.service.Platform1DeviceApiHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceOwnerController {

    private final Platform1DeviceApiHandler platform1DeviceApiHandler;
    private final DeviceOwnerService deviceOwnerService;

    @InternalHandler
    @PostMapping("/api/event/beta/refresh/{username}")
    public ApiResponse<Void> refresh(@PathVariable String username) {
        var deviceInfos = platform1DeviceApiHandler.getDeviceList(username);
        deviceOwnerService.updateAll(username, deviceInfos);
        log.info("Refresh succeed : {}", username);
        return ApiResponse.success();
    }

}
