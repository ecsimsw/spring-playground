package com.ecsimsw.event.controller;

import com.ecsimsw.event.config.MqttConfig;
import com.ecsimsw.event.service.DeviceEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TbDeviceEventConsumer {

    private final DeviceEventHandler deviceEventHandler;
    private final ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = MqttConfig.MQTT_INPUT_CHANNEL_NAME)
    public void handle(Message<String> message) {

    }
}