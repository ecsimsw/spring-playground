package com.ecsimsw.springsdkexternalplatform.dto;

import java.util.List;

public record DeviceEventMessage(
    String deviceId,
    String productId,
    List<DeviceStatus> statuses
) {
}

