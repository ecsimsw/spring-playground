package com.ecsimsw.device.dto;

import com.ecsimsw.device.domain.DeviceHistory;

import java.time.LocalDateTime;

public record DeviceHistoryResponse(
    String id,
    String deviceId,
    String historyCode,
    Object historyValue,
    LocalDateTime timestamp
) {

    public static DeviceHistoryResponse of(DeviceHistory deviceHistory) {
        return new DeviceHistoryResponse(
            deviceHistory.getId(),
            deviceHistory.getDeviceId(),
            deviceHistory.getHistoryCode(),
            deviceHistory.getHistoryValue(),
            deviceHistory.getTimestamp()
        );
    }
}
