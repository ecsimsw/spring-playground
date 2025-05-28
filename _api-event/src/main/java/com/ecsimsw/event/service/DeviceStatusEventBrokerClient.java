package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceStatusEventBrokerClient {

    @Value("${kafka.device.status.topic}")
    private String deviceStatusTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceDeviceStatus(DeviceStatusEvent statusEvent) {
        var jsonMessage = convertAsJson(statusEvent);
        kafkaTemplate.send(deviceStatusTopic, statusEvent.getDeviceId(), jsonMessage)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("Thread id : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device status event : {}", statusEvent.getDeviceId());
    }

    private String convertAsJson(DeviceStatusEvent statusEvent) {
        try {
            return objectMapper.writeValueAsString(statusEvent);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}
