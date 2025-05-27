package com.ecsimsw.event.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
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

    private final KafkaTemplate<String, DeviceStatusEvent> kafkaTemplate;

    public void produceDeviceStatus(DeviceStatusEvent statusEvent) {
        kafkaTemplate.send(deviceStatusTopic, statusEvent.deviceId(), statusEvent)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.info("Thread id : {} ", (Thread.currentThread().getName()));
                }
            });
    }
}
