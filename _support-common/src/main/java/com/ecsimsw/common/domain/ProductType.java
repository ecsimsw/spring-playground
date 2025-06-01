package com.ecsimsw.common.domain;

import java.util.ArrayList;
import java.util.List;

public enum ProductType {
    Camera(
        List.of(
            new DeviceCode("indicator", "basic_indicator", Boolean.class),
            new DeviceCode("privateMode", "basic_private", Boolean.class),
            new DeviceCode("motionDetect", "motion_switch", Boolean.class)
        ),
        List.of(
            new DeviceCode("linkage", "linkage", Boolean.class)
        )
    ),
    Brunt(
        List.of(
            new DeviceCode("switch", "switch_led", Boolean.class),
            new DeviceCode("bright", "bright_value", Integer.class),
            new DeviceCode("mode", "work_mode", String.class)
        )
    ),
    Plug(
        List.of(
            new DeviceCode("switch", "switch_1", Boolean.class)
        )
    ),
    Power(
        List.of(
            new DeviceCode("switch", "", Boolean.class)
        )
    );

    public final List<DeviceCode> statusCodes;
    public final List<DeviceCode> alertCodes;

    ProductType(List<DeviceCode> statusCodes) {
        this(statusCodes, new ArrayList<>());
    }

    ProductType(List<DeviceCode> statusCodes, List<DeviceCode> alertCodes) {
        this.statusCodes = statusCodes;
        this.alertCodes = alertCodes;
    }
}
