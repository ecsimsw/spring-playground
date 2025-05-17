package com.ecsimsw.common.service.client;

import com.ecsimsw.common.service.InternalCommunicateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Service
public class AccountClient {

    private final InternalCommunicateService internalCommunicateService;

    public ResponseEntity<Void> addCredit(String username, Long addition) {
        return internalCommunicateService.request(POST, "/api/account/credit?addition=" + addition + "&username=" + username, Void.class);
    }

    public ResponseEntity<Void> rollbackCreditAddition(String username, Long addition) {
        return internalCommunicateService.request(POST, "/api/account/credit/rollback?addition=" + addition + "&username=" + username, Void.class);
    }
}
