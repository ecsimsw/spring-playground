package com.ecsimsw.event.support;

import com.ecsimsw.common.dto.DeviceAlertEvent;
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
public class DeviceEventBrokerClient {

    @Value("${kafka.device.status.topic}")
    private String deviceStatusTopic;

    @Value("${kafka.device.alert.topic}")
    private String deviceEventTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceDeviceStatus(DeviceStatusEvent statusEvent) {
        var jsonMessage = convertAsJson(statusEvent);
        kafkaTemplate.send(deviceStatusTopic, statusEvent.getDeviceId(), jsonMessage)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("failed to produce device status event : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device status event : {}", statusEvent.getDeviceId());
    }

    public void produceDeviceAlert(DeviceAlertEvent alertEvent) {
        var jsonMessage = convertAsJson(alertEvent);
        kafkaTemplate.send(deviceEventTopic, alertEvent.getDeviceId(), jsonMessage)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("failed to produce device alert event : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device alert event : {}", alertEvent.getDeviceId());
    }

    private String convertAsJson(Object objectMessage) {
        try {
            return objectMapper.writeValueAsString(objectMessage);
        } catch (Exception e) {
            log.error("Failed to parse json");
            throw new IllegalArgumentException(e);
        }
    }
}
