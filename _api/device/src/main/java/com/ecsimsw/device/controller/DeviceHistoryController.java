package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.DeviceHistoryEvent;
import com.ecsimsw.device.service.DeviceHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeviceHistoryController {

    private final DeviceHistoryService deviceHistoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.device.history.topic}",
        groupId = "${kafka.device.history.groupId}",
        concurrency = "${kafka.device.history.partitionCount}"
    )
    public void listenHistory(String message) {
        var statusEvent = convertFromJson(message);
        log.info("Handle device history event {} {}", statusEvent.getDeviceId(), statusEvent.statusAsMap());
        deviceHistoryService.save(statusEvent);
    }

    private DeviceHistoryEvent convertFromJson(String historyEvent) {
        try {
            return objectMapper.readValue(historyEvent, DeviceHistoryEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}
