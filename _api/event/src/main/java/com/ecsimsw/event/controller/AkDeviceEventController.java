package com.ecsimsw.event.controller;

import com.ecsimsw.event.service.DeviceEventHandler;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.event.DeviceEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AkDeviceEventController {

    private final DeviceEventHandler deviceEventHandler;
    private final Random random = new Random();

    @PutMapping("/api/event/ak/{deviceId}/logs")
    public ResponseEntity<Void> uploadAkLog(
        @PathVariable String deviceId,
        @RequestBody DeviceEventMessage message
    ) {
        deviceEventHandler.handleDeviceEvent(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/event/ak/{deviceId}/motion")
    public ResponseEntity<Void> uploadAkMotion(
        @PathVariable String deviceId
    ) {
        log.info("motion detected {}", deviceId);
        deviceEventHandler.handleDeviceEvent(new DeviceEventMessage(
            deviceId,
            "e16c186b2e2b",
            List.of(new CommonDeviceStatus("motionDetected", Map.of(
                "motionType", getMotionType(),
                "detected_x", random.nextFloat(100),
                "detected_y", random.nextFloat(100)))
            ),
            System.currentTimeMillis()
        ));
        return ResponseEntity.ok().build();
    }

    private String getMotionType() {
        var rand = random.nextInt(100);
        if (rand <= 30) {
            return "Small movement";
        }
        if (rand <= 60) {
            return "Medium movement";
        }
        return "Large movement";
    }
}
