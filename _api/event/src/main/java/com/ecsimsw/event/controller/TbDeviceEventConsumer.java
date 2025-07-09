package com.ecsimsw.event.controller;

import com.ecsimsw.event.config.MqttConfig;
import com.ecsimsw.event.service.DeviceEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TbDeviceEventConsumer {

    private final DeviceEventHandler deviceEventHandler;

    @ServiceActivator(inputChannel = MqttConfig.MQTT_INPUT_CHANNEL_NAME)
    public void handle(Message<?> message) {
        System.out.println("MQTT 수신 받은 메시지: " + message.getPayload());
    }
}