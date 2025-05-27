package com.ecsimsw.common.dto;

public record DeviceStatusEvent(
    String deviceId,
    String statusCode,
    Object statusValue
) {
}
