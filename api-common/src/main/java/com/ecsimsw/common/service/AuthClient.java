package com.ecsimsw.common.service;

import com.ecsimsw.common.service.dto.AuthCreationRequest;
import com.ecsimsw.common.service.dto.AuthUpdateRequest;
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
        return internalCommunicateService.request(Void.class, POST, "/api/auth/user", request);
    }

    public ResponseEntity<Void> updateAuth(AuthUpdateRequest request) {
        return internalCommunicateService.request(Void.class, PUT, "/api/auth/user", request);
    }
}
