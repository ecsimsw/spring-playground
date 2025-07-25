package com.ecsimsw.common.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record DeviceHistoryEvent(
    String deviceId,
    String code,
    Object value,
    LocalDateTime timestamp,
    String messageId
) {

    public DeviceHistoryEvent(String deviceId, String code, Object value) {
        this(deviceId, code, value, LocalDateTime.now(), UUID.randomUUID().toString());
    }

    public DeviceHistoryEvent(String deviceId, String code, Object value, LocalDateTime timestamp) {
        this(deviceId, code, value, timestamp, UUID.randomUUID().toString());
    }

    public Map<String, Object> statusAsMap() {
        return Map.of(code, value);
    }
}
