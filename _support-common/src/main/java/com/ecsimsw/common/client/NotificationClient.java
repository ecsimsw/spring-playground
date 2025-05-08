package com.ecsimsw.common.client;

import com.ecsimsw.common.service.InternalCommunicateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Service
public class NotificationClient {

    private final InternalCommunicateService internalCommunicateService;

    public Mono<ResponseEntity<Void>> createNotificationAsync(String message) {
        return internalCommunicateService.requestAsync(POST, "/api/notification?message=" + message, Void.class);
    }

    public ResponseEntity<Void> createNotification(String message) {
        return internalCommunicateService.request(POST, "/api/notification?message=" + message, Void.class);
    }
}
