package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.device.service.DeviceAlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceAlertController {

    private final DeviceAlertService deviceAlertService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.device.alert.topic}",
        groupId = "${kafka.device.alert.groupId}",
        concurrency = "${kafka.device.alert.partitionCount}"
    )
    public void listenDeviceAlert(String event) {
        var deviceAlertEvent = convertFromJson(event);
        deviceAlertService.alert(deviceAlertEvent);
    }

    private DeviceAlertEvent convertFromJson(String alertEvent) {
        try {
            return objectMapper.readValue(alertEvent, DeviceAlertEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}
