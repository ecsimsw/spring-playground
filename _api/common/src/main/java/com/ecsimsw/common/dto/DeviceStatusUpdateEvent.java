package com.ecsimsw.common.dto;

import java.util.Map;

public record DeviceStatusUpdateEvent(
    String deviceId,
    Map<String, Object> status
) {
}
