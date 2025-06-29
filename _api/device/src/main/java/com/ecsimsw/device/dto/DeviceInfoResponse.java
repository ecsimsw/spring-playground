package com.ecsimsw.device.dto;

import com.ecsimsw.device.domain.BindDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record DeviceInfoResponse(
    String deviceId,
    String productId,
    String name,
    boolean online,
    Map<String, Object> status
) {

    public static DeviceInfoResponse of(BindDevice device) {
        return new DeviceInfoResponse(
            device.getDeviceId(),
            device.getProductId(),
            device.getName(),
            device.isOnline(),
            device.getStatus()
        );
    }
}
