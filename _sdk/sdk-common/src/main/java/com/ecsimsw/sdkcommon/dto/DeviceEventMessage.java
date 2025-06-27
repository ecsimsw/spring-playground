package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record DeviceEventMessage(
    String deviceId,
    String productId,
    List<DeviceStatusValue> statuses
) {
}

