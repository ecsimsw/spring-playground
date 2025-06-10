package com.ecsimsw.springsdkexternalplatform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ExternalWebClientConfig {

    @Bean
    public static WebClient webClientExternal() {
        return WebClient.builder()
            .build();
    }
}

