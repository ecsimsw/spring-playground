package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MqttBetaHandlerService {

    private final DeviceStatusService deviceStatusService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void handle(String topic, String payload) {
        log.info(topic + " : " + payload);
        if(topic.equals("stat/hejspm_12C724/RESULT")) {
            Map<String, String> map = objectMapper.readValue(payload, Map.class);
            for(var deviceId : map.keySet()) {
                var value = map.get(deviceId);
                if(value.equals("ON")) {
                    deviceStatusService.updateStatus(new DeviceStatusEvent(deviceId, "switch", true));
                }
                if(value.equals("OFF")) {
                    deviceStatusService.updateStatus(new DeviceStatusEvent(deviceId, "switch", false));
                }
            }
        }
    }
}