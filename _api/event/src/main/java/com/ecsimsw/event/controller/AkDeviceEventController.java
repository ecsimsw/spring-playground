package com.ecsimsw.event.controller;

import com.ecsimsw.event.service.DeviceEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AkDeviceEventController {

    private final DeviceEventHandler deviceEventHandler;

    @PutMapping("/api/event/ak/{deviceId}/logs")
    public void uploadAkLog(@PathVariable String deviceId, String message) {
        System.out.println(message);
    }
}
