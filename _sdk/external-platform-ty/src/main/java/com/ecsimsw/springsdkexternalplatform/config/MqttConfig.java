package com.ecsimsw.springsdkexternalplatform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MqttConfig {

    @Value("${mqtt.device.broker}")
    public String brokerEndpoint;

}