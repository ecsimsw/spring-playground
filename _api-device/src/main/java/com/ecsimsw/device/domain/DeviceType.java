package com.ecsimsw.device.domain;

import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.device.error.DeviceException;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public enum DeviceType {
    Camera(
        List.of(""),
        new DeviceStatusCode("online", Boolean.class)
    ),
    Brunt(
        List.of(""),
        new DeviceStatusCode("online", Boolean.class)
    ),
    Plug(
        List.of("uxjr57hvapakd0io"),
        new DeviceStatusCode("switch_1", Boolean.class)
    );

    private final List<String> productIds;
    private final List<DeviceStatusCode> statusCodes;

    DeviceType(List<String> productIds, DeviceStatusCode... statusCodes) {
        this.productIds = productIds;
        this.statusCodes = List.of(statusCodes);
    }

    public static DeviceType resolveByProductId(String productId) {
        return Arrays.stream(values())
            .filter(it -> it.productIds.contains(productId))
            .findAny()
            .orElseThrow(() -> new DeviceException(ErrorType.NOT_SUPPORTED_DEVICE));
    }

    public static boolean isSupportedProduct(String productId) {
        return Arrays.stream(values())
            .anyMatch(it -> it.productIds.contains(productId));
    }

    public boolean isSupportedStatusCode(String statusCode) {
        return statusCodes.stream()
            .anyMatch(it -> it.name().equals(statusCode));
    }

    public DeviceStatusCode getDeviceStatusCode(String statusCode) {
        return statusCodes.stream()
            .filter(it -> it.name().equals(statusCode))
            .findAny()
            .orElseThrow(() -> new DeviceException(ErrorType.NOT_SUPPORTED_DEVICE));
    }

    public List<DeviceStatusCode> statusCodes() {
        return new ArrayList<>(statusCodes);
    }
}
