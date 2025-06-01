package com.ecsimsw.common.domain;

import java.util.List;

public enum ProductType {
    Camera(
        new DeviceStatusCode("indicator", Boolean.class),
        new DeviceStatusCode("privateMode", Boolean.class)
    ),
    Brunt(
        new DeviceStatusCode("switch", Boolean.class),
        new DeviceStatusCode("bright", Integer.class),
        new DeviceStatusCode("mode", String.class)
    ),
    Plug(
        new DeviceStatusCode("switch", Boolean.class)
    );

    public final List<DeviceStatusCode> statusCodes;

    ProductType(DeviceStatusCode... statusCodes) {
        this.statusCodes = List.of(statusCodes);
    }
}
