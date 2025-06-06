package com.ecsimsw.event.config;

import com.ecsimsw.springsdkexternalplatform.config.PulsarAuthentication;
import com.ecsimsw.springsdkexternalplatform.service.PulsarBrokerHandler;
import com.ecsimsw.springsdkexternalplatform.service.PulsarBrokerListenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EventConfig {

    @Value("${pulsar.event.url}")
    private String serverUrl;

    @Value("${pulsar.event.accessId}")
    private String accessId;

    @Value("${pulsar.event.accessKey}")
    private String accessKey;

    @SneakyThrows
    @Bean
    public PulsarClient pulsarClient() {
        return PulsarClient.builder()
            .serviceUrl(serverUrl)
            .allowTlsInsecureConnection(true)
            .authentication(new PulsarAuthentication(accessId, accessKey))
            .build();
    }

    @Bean
    public PulsarBrokerListenerService pulsarBrokerListenerService(
        @Value("${pulsar.event.secretKey}")
        String secretKey,
        @Value("${pulsar.event.topic}")
        String topic,
        @Value("${pulsar.event.subscriptionName}")
        String subscriptionName,
        PulsarBrokerHandler pulsarBrokerHandler
    ) {
        return new PulsarBrokerListenerService(
            secretKey,
            topic,
            subscriptionName,
            pulsarClient(),
            pulsarBrokerHandler
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
