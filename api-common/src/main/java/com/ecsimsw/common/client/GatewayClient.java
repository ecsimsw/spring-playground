package com.ecsimsw.common.client;

import com.ecsimsw.common.domain.ServiceName;
import com.ecsimsw.common.service.InternalCommunicateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Service
public class GatewayClient {

    private final InternalCommunicateService internalCommunicateService;

    public ResponseEntity<Void> registerService(ServiceName serviceName, int port) {
        return internalCommunicateService.request(
            POST,
            "/api/gateway/service?service=" + serviceName.name() + "&port=" + port,
            Void.class
        );
    }

    public ResponseEntity<Void> removeService(ServiceName serviceName, int port) {
        return internalCommunicateService.request(
            DELETE,
            "/api/gateway/service?service=" + serviceName.name() + "&port=" + port,
            Void.class
        );
    }
}
