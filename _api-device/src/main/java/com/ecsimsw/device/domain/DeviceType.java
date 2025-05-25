package com.ecsimsw.device.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public enum DeviceType {
    Camera(
        Set.of(""),
        new DeviceStatusCode("online", Boolean.class)
    ),
    Brunt(
        Set.of(""),
        new DeviceStatusCode("online", Boolean.class)
    ),
    Plug(
        Set.of("uxjr57hvapakd0io"),
        new DeviceStatusCode("switch_1", Boolean.class)
    );

    private final Set<String> productKeys;
    private final Set<DeviceStatusCode> statusCode;

    DeviceType(Set<String> productKeys, DeviceStatusCode... statusCodes) {
        this.productKeys = productKeys;
        this.statusCode = Set.of(statusCodes);
    }

    public static Optional<DeviceType> findTypeByProductId(String productKey) {
        return Arrays.stream(values())
            .filter(it -> it.productKeys.contains(productKey))
            .findAny();
    }

    public Set<DeviceStatusCode> statusCode() {
        return new HashSet<>(statusCode);
    }
}
