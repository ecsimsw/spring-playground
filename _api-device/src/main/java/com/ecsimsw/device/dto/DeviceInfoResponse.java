package com.ecsimsw.device.dto;

import java.util.Map;

public record DeviceInfoResponse(
    String deviceId,
    String deviceName,
    String productName,
    boolean online,
    Map<String, Object> status
) {
}
