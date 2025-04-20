package com.ecsimsw.apievent.config;

import com.ecsimsw.apievent.support.PulsarAuthentication;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PulsarClient pulsarClient() throws PulsarClientException {
        return PulsarClient.builder()
            .serviceUrl(serverUrl)
            .allowTlsInsecureConnection(true)
            .authentication(new PulsarAuthentication(accessId, accessKey))
            .build();
    }
}
