package com.ecsimsw.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeviceStatusEvent {

    private final String deviceId;
    private final String code;
    private final Object value;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String messageHash = UUID.randomUUID().toString();
}
