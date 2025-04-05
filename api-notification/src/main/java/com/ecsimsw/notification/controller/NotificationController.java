package com.ecsimsw.notification.controller;

import com.ecsimsw.common.error.AuthException;
import com.ecsimsw.common.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        throw new IllegalArgumentException("sdfadf");
//        return ResponseEntity.ok().build();
    }
}
