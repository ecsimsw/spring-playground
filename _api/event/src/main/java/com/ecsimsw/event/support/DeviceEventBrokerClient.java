package com.ecsimsw.event.support;

import com.ecsimsw.common.dto.DeviceAlertEvent;
import com.ecsimsw.common.dto.DeviceHistoryEvent;
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

    @Value("${kafka.device.history.topic}")
    private String deviceHistoryTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceDeviceStatus(DeviceStatusEvent statusEvent) {
        kafkaTemplate.send(deviceStatusTopic, statusEvent.deviceId(), convertAsJson(statusEvent))
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("failed to produce device status event : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device status event : {}", statusEvent.deviceId());
    }

    public void produceDeviceHistory(DeviceHistoryEvent historyEvent) {
        kafkaTemplate.send(deviceHistoryTopic, historyEvent.deviceId(), convertAsJson(historyEvent))
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("failed to produce device history event : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device history event : {}", historyEvent.deviceId());
    }

    public void produceDeviceAlert(DeviceAlertEvent alertEvent) {
        kafkaTemplate.send(deviceEventTopic, alertEvent.deviceId(), convertAsJson(alertEvent))
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("failed to produce device alert event : {} ", (Thread.currentThread().getName()));
                }
            });
        log.info("produce device alert event : {}", alertEvent.deviceId());
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
