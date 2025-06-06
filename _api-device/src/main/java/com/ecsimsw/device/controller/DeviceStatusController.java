package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.device.dto.DeviceInfoResponse;
import com.ecsimsw.device.service.DeviceStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/device/{deviceId}")
    public ApiResponse<DeviceInfoResponse> status(AuthUser authUser, @PathVariable String deviceId) {
        var result = deviceStatusService.readStatus(authUser.username(), deviceId);
        return ApiResponse.success(result);
    }

    @KafkaListener(
        topics = "${kafka.device.status.topic}",
        groupId = "${kafka.device.status.groupId}",
        concurrency = "${kafka.device.status.partitionCount}"
    )
    public void listenDeviceStatus(String message) {
        var statusEvent = convertFromJson(message);
        deviceStatusService.updateStatus(statusEvent);
        deviceStatusService.sendSocket(statusEvent);
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
