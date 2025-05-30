package com.ecsimsw.common.domain;

import java.util.List;

public enum ProductType {
    Camera(
        new DeviceStatusCode("switch_led", Boolean.class),
        new DeviceStatusCode("bright_value", Integer.class),
        new DeviceStatusCode("work_mode", String.class)
    ),
    Brunt(
        new DeviceStatusCode("switch_led", Boolean.class),
        new DeviceStatusCode("bright_value", Integer.class),
        new DeviceStatusCode("work_mode", String.class)
    ),
    Plug(
        new DeviceStatusCode("switch_1", Boolean.class)
    );

    public final List<DeviceStatusCode> statusCodes;

    ProductType(DeviceStatusCode... statusCodes) {
        this.statusCodes = List.of(statusCodes);
    }
}
