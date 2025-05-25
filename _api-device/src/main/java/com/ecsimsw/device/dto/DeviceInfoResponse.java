package com.ecsimsw.device.dto;

import java.util.Map;

public record DeviceInfoResponse(
    String deviceId,
    String productId,
    boolean online,
    Map<String, Object> status
) {
}
