package com.ecsimsw.common.config;

import com.ecsimsw.common.client.GatewayClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ServiceDiscoveryConfig {

    private final GatewayClient gatewayClient;

    @Value("${server.port}")
    private int port;

    @Value("${service.name}")
    private String serviceName;

    @PostConstruct
    public void init() {
        var service = ServiceName.resolve(serviceName);
        if(service == ServiceName.GATEWAY) {
            return;
        }
        var result = gatewayClient.registerService(service, port);
        if(result.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Failed to register service " + serviceName);
        }
        log.info("try register service : {}", service);
    }

    @PreDestroy
    public void destroy() {
        var service = ServiceName.resolve(serviceName);
        if(service == ServiceName.GATEWAY) {
            return;
        }
        gatewayClient.removeService(service, port);
        log.info("try remove service : {}", service);
    }
}
