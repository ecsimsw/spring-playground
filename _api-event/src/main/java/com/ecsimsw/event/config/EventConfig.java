package com.ecsimsw.event.config;

import com.ecsimsw.springsdkexternalplatform.config.PulsarAuthentication;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EventConfig {

    @Bean
    public PulsarClient pulsarClient(
        @Value("${pulsar.event.url}")
        String serverUrl,
        @Value("${pulsar.event.accessId}")
        String accessId,
        @Value("${pulsar.event.accessKey}")
        String accessKey
    ) throws PulsarClientException {
        return PulsarClient.builder()
            .serviceUrl(serverUrl)
            .allowTlsInsecureConnection(true)
            .authentication(new PulsarAuthentication(accessId, accessKey))
            .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
