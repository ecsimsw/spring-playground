package com.ecsimsw.auth.controller;

import com.ecsimsw.auth.service.AuthService;
import com.ecsimsw.common.annotation.InternalHandler;
import com.ecsimsw.common.service.dto.AuthCreationRequest;
import com.ecsimsw.common.service.dto.AuthUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthAdminController {

    private final AuthService authService;

    @InternalHandler
    @PostMapping("/api/auth/user")
    public ResponseEntity<Void> createUser(@RequestBody AuthCreationRequest request) {
        authService.createUserAuth(request);
        return ResponseEntity.ok().build();
    }

    @InternalHandler
    @PutMapping("/api/auth/user")
    public ResponseEntity<Void> updateUser(@RequestBody AuthUpdateRequest request) {
        authService.updateUserAuth(request);
        return ResponseEntity.ok().build();
    }
}
