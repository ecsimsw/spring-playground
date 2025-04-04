package com.ecsimsw.common.domain;

import java.util.Arrays;

public enum ServiceName {
    GATEWAY,
    USER,
    AUTH,
    TRANSACTION,
    NOTIFICATION;

    public static ServiceName resolve(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findAny()
            .orElseThrow();
    }
}
