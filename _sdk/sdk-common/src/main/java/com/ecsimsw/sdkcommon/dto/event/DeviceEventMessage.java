package com.ecsimsw.sdkcommon.dto.event;

import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;

import java.util.List;

public record DeviceEventMessage(
    String deviceId,
    String productId,
    List<CommonDeviceStatus> statuses
) {
}

