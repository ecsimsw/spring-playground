package com.ecsimsw.common.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceAlertEvent(
    String deviceId,
    String username,
    String productId,
    String code,
    Object value,
    LocalDateTime timestamp,
    String messageId
) {

    public DeviceAlertEvent(String deviceId, String username, String productId, String code, Object value) {
        this(deviceId, username, productId, code, value, LocalDateTime.now(), UUID.randomUUID().toString());
    }
}
