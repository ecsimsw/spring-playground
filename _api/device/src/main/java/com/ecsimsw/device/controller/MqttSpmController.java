package com.ecsimsw.device.controller;

import com.ecsimsw.common.dto.DeviceStatusEvent;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.error.DeviceException;
import com.ecsimsw.device.service.DeviceEventWebSocketService;
import com.ecsimsw.device.service.DeviceStatusService;
import com.ecsimsw.sdkty.domain.PlatformProducts;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MqttSpmController {

    private final DeviceStatusService deviceStatusService;
    private final DeviceEventWebSocketService deviceEventWebSocketService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void handle(String topic, String payload) {
        log.info(topic + " : " + payload);
//        if(topic.contains(PlatformProducts.PRODUCTS.Power.productId)) {
            Map<String, String> map = objectMapper.readValue(payload, Map.class);
            for(var deviceId : map.keySet()) {
                var statusEvent = parseStatusEvent(deviceId, map);
                deviceStatusService.updateStatus(statusEvent);
                deviceEventWebSocketService.sendMessage("", objectMapper.writeValueAsString(statusEvent));
            }
//        }
    }

    private static DeviceStatusEvent parseStatusEvent(String deviceId, Map<String, String> map) {
        var value = map.get(deviceId);
        if(value.equals("ON")) {
            return new DeviceStatusEvent(deviceId, "switch", true);
        }
        if(value.equals("OFF")) {
            return new DeviceStatusEvent(deviceId, "switch", false);
        }
        throw new DeviceException(ErrorType.INVALID_DEVICE);
    }
}