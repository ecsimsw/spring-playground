package com.ecsimsw.event.config;

import com.ecsimsw.common.service.TbEventConsumer;
import com.ecsimsw.event.service.DeviceEventHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class PlatformTbEventBrokerConfig {

    @Value("${tb.mqtt.device.broker}")
    private String serverUrl;

    private final DeviceEventHandler deviceEventHandler;

    @Bean
    public TbEventConsumer tbEventConsumer() {
        return new TbEventConsumer(
            serverUrl,
            deviceEventHandler
        );
    }
}
