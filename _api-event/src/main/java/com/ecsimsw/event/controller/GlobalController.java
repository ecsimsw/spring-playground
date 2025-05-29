package com.ecsimsw.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GlobalController {

    @Value("${VERSION:unknown}")
    private String version;

    @GetMapping("/api/event/up")
    public ResponseEntity<String> health() {
        log.info("up : {}", version);
        return ResponseEntity.ok(version);
    }
}
