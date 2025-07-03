package com.ecsimsw.sdkcommon.dto.event;

import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public record DeviceEventMessage(
    String deviceId,
    String productId,
    List<CommonDeviceStatus> statuses,
    Long timestamp
) {

    public LocalDateTime timeStamp() {
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime();
    }
}

