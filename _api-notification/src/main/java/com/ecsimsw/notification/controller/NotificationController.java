package com.ecsimsw.notification.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotificationController {

    @PostMapping("/api/notification")
    public ResponseEntity<Void> ok(@RequestParam String message) {
        log.info("message : {}", message);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        throw new IllegalArgumentException("Test error");
    }

    @GetMapping("/api/notification/up")
    public ResponseEntity<Void> up() {
        return ResponseEntity.ok().build();
    }
}
