package com.ecsimsw.common.domain;

import java.util.ArrayList;
import java.util.List;

public enum ProductType {
    Camera(
        List.of(
            new DeviceCode("indicator", Boolean.class),
            new DeviceCode("privateMode", Boolean.class),
            new DeviceCode("motionDetect", Boolean.class)
        ),
        List.of(
            new DeviceCode("linkage", Boolean.class)
        )
    ),
    Brunt(
        List.of(
            new DeviceCode("switch", Boolean.class),
            new DeviceCode("bright", Integer.class),
            new DeviceCode("mode", String.class)
        )
    ),
    Plug(
        List.of(
            new DeviceCode("switch", Boolean.class)
        )
    ),
    Power(
        List.of(
            new DeviceCode("switch", Boolean.class)
        )
    ),
    Presence_Sensor(
        List.of(
            new DeviceCode("human_motion_state", Boolean.class)
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
