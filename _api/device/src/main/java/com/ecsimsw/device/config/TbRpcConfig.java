package com.ecsimsw.device.config;

import com.ecsimsw.common.service.TbRpcListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TbRpcConfig {

    @Value("${tb.mqtt.device.broker}")
    private String brokerUrl;

    @Bean
    public TbRpcListener tbRpcListener() {
        return new TbRpcListener(brokerUrl);
    }
}
