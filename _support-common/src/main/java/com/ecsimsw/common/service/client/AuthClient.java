package com.ecsimsw.common.service.client;

import com.ecsimsw.common.service.InternalCommunicateService;
import com.ecsimsw.common.service.client.dto.AuthCreationRequest;
import com.ecsimsw.common.service.client.dto.AuthUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@RequiredArgsConstructor
@Service
public class AuthClient {

    private final InternalCommunicateService internalCommunicateService;

    public ResponseEntity<Void> createAuth(AuthCreationRequest request) {
        return internalCommunicateService.request(POST, "/api/auth/user", request, Void.class);
    }

    public ResponseEntity<Void> updateAuth(AuthUpdateRequest request) {
        return internalCommunicateService.request(PUT, "/api/auth/user", request, Void.class);
    }
}
