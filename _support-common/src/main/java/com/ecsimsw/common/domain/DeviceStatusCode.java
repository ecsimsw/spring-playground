package com.ecsimsw.common.domain;

public record DeviceStatusCode(
    String name,
    Class<?> type
) {
    public Object asValue(String value) {
        if(type == Integer.class) {
            return Integer.parseInt(value);
        }
        if(type == Float.class) {
            return Float.parseFloat(value);
        }
        return value;
    }
}
