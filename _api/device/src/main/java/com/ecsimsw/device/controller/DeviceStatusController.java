package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceBindService;
import com.ecsimsw.device.service.DeviceStatusService;
import com.ecsimsw.sdktb.service.TbApiService;
import com.ecsimsw.sdkty.service.TyApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceStatusController {

    private final TyApiService tyApiService;
    private final DeviceStatusService deviceStatusService;
    private final DeviceBindService deviceBindService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/device/{deviceId}")
    public ApiResponse<DeviceInfoResponse> getStatus(AuthUser authUser, @PathVariable String deviceId) {
        var result = deviceStatusService.readStatus(authUser.username(), deviceId);
        return ApiResponse.success(result);
    }

    @PostMapping("/api/device/{deviceId}")
    public ApiResponse<Void> control(
        AuthUser authUser,
        @PathVariable String deviceId,
        @RequestBody List<Map<String, Object>> commonDeviceStatuses
    ) {
        var bindDevice = deviceBindService.getUserDevice(authUser.username(), deviceId);
        tyApiService.command(deviceId, bindDevice.productId(), commonDeviceStatuses);
        return ApiResponse.success();
    }

    @KafkaListener(
        topics = "${kafka.device.status.topic}",
        groupId = "${kafka.device.status.groupId}",
        concurrency = "${kafka.device.status.partitionCount}"
    )
    public void listenStatus(String message) {
        try {
            var statusEvent = convertFromJson(message);
            log.info("Handle device status event {} {}", statusEvent.deviceId(), statusEvent.statusAsMap());
            deviceStatusService.updateStatus(statusEvent);
            deviceStatusService.sendSocket(statusEvent);
        } catch (Exception e) {
//            log.error(e.getMessage());
        }
    }

    private DeviceStatusEvent convertFromJson(String statusEvent) {
        try {
            return objectMapper.readValue(statusEvent, DeviceStatusEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}
