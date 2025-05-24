package com.ecsimsw.device.dto;

import com.ecsimsw.device.domain.DeviceType;

import java.util.Map;

public record DeviceInfoResponse(
    String deviceId,
    String deviceName,
    DeviceType deviceType,
    Map<String, String> essentialStatus
) {
}
