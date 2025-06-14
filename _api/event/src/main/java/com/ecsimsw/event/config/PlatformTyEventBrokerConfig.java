package com.ecsimsw.event.config;

import com.ecsimsw.event.service.DeviceEventHandler;
import com.ecsimsw.sdkty.config.PulsarAuthentication;
import com.ecsimsw.sdkty.service.TyEventConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class PlatformTyEventBrokerConfig {

    private final ObjectMapper objectMapper;
    private final DeviceEventHandler deviceEventHandler;

    @SneakyThrows
    @Bean
    public PulsarClient pulsarClient(
        @Value("${ty.pulsar.event.url}")
        String serverUrl,
        @Value("${ty.pulsar.event.accessId}")
        String accessId,
        @Value("${ty.pulsar.event.accessKey}")
        String accessKey
    ) {
        return PulsarClient.builder()
            .serviceUrl(serverUrl)
            .allowTlsInsecureConnection(true)
            .authentication(new PulsarAuthentication(accessId, accessKey))
            .build();
    }

    @Bean
    public TyEventConsumer tyEventConsumer(
        PulsarClient pulsarClient,
        @Value("${ty.pulsar.event.secretKey}")
        String secretKey,
        @Value("${ty.pulsar.event.topic}")
        String topic,
        @Value("${ty.pulsar.event.subscriptionName}")
        String subscriptionName,
        @Value("${ty.pulsar.event.partitionNumber}")
        int partitionNumber
    ) {
        return new TyEventConsumer(
            pulsarClient,
            secretKey,
            topic,
            subscriptionName,
            partitionNumber,
            objectMapper,
            deviceEventHandler
        );
    }
}
