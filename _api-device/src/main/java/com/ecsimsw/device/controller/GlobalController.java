package com.ecsimsw.device.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GlobalController {

    @GetMapping("/api/device/up")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }
}
