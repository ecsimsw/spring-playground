package com.ecsimsw.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GlobalController {

    @Value("${VERSION:unknown}")
    private String version;

    @GetMapping("/api/account/up")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(version);
    }
}
