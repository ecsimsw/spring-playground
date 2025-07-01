package com.ecsimsw.event.config;

import com.ecsimsw.sdkty.config.PulsarAuthentication;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class PlatformTyEventBrokerConfig {

    @SneakyThrows
    @Bean
    public PulsarClient pulsarClient(
        @Value("${ty.pulsar.event.url}")
        String serverUrl,
        @Value("${ty.auth.accessId}")
        String accessId,
        @Value("${ty.auth.accessKey}")
        String accessKey
    ) {
        return PulsarClient.builder()
            .serviceUrl(serverUrl)
            .allowTlsInsecureConnection(true)
            .authentication(new PulsarAuthentication(accessId, accessKey))
            .build();
    }
}
