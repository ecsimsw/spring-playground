package com.ecsimsw.event.controller;

import com.ecsimsw.event.service.DeviceEventHandler;
import com.ecsimsw.sdkcommon.dto.event.DeviceEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AkDeviceEventController {

    private final DeviceEventHandler deviceEventHandler;

    @PutMapping("/api/event/ak/{deviceId}/logs")
    public ResponseEntity<Void> uploadAkLog(@PathVariable String deviceId, @RequestBody DeviceEventMessage message) {
        deviceEventHandler.handleDeviceEvent(message);
        return ResponseEntity.ok().build();
    }
}
