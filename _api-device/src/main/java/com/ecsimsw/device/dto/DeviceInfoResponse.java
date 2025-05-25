package com.ecsimsw.device.dto;

import java.util.Map;

public record DeviceInfoResponse(
    String deviceId,
    String deviceName,
    String productName,
    Map<String, Object> status
) {
}
