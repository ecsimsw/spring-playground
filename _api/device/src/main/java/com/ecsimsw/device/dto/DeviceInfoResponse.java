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
        var product = device.getProduct();
        var deviceStatus = device.getStatus();
        var deviceStatusMap = deviceStatus.keySet().stream()
            .filter(product::hasStatusCode)
            .collect(Collectors.toMap(statusCode -> statusCode, deviceStatus::get));
        return new DeviceInfoResponse(
            device.getDeviceId(),
            device.getProduct().id(),
            device.getName(),
            device.isOnline(),
            deviceStatusMap
        );
    }
}
