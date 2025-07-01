package com.ecsimsw.event.controller;

import com.ecsimsw.event.service.DeviceEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AkDeviceEventController {

    private final DeviceEventHandler deviceEventHandler;

    @GetMapping("/api/event/ak/logs")
    public void fetchLogs(String message) {
        System.out.println(message);
    }
}
