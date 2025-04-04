package com.ecsimsw.common.client;

import com.ecsimsw.common.service.InternalCommunicateService;
import com.ecsimsw.common.client.dto.AuthCreationRequest;
import com.ecsimsw.common.client.dto.AuthUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@RequiredArgsConstructor
@Service
public class UserClient {

    private final InternalCommunicateService internalCommunicateService;

    public ResponseEntity<Void> addCredit(Long addition) {
        return internalCommunicateService.request(POST, "/api/auth/user?addition="+addition, Void.class);
    }
}
