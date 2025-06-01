package com.ecsimsw.notification.controller;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.notification.service.DeviceAlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AlertEventController {

    private final DeviceAlertService deviceAlertService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.device.alert.topic}",
        groupId = "${kafka.device.alert.groupId}",
        concurrency = "${kafka.device.alert.partitionCount}"
    )
    public void listenDeviceAlert(String event) {
        var deviceAlertEvent = convertFromJson(event);
        deviceAlertService.readAlert(deviceAlertEvent);
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
