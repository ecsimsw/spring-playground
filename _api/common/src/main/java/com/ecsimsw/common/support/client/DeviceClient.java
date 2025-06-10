package com.ecsimsw.common.support.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Service
public class DeviceClient {

    private final InternalCommunicateService internalCommunicateService;

    public ResponseEntity<Void> refresh(String username) {
        return internalCommunicateService.request(POST, "/api/device/beta/refresh/" + username, Void.class);
    }
}
