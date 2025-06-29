package com.ecsimsw.sdkcommon.domain;

public record DeviceCode(
    String name,
    Class<?> type
) {
    public Object convertValue(Object value) {
        if(value == null) {
            return null;
        }
        if(type.isInstance(value)) {
            return value;
        }
        if(value instanceof String string) {
            if(type == Integer.class) {
                return Integer.parseInt(string);
            }
            if(type == Float.class) {
                return Float.parseFloat(string);
            }
            if(type == Double.class) {
                return Double.parseDouble(string);
            }
            if(type == Boolean.class) {
                return Boolean.parseBoolean(string);
            }
            return string;
        }
        return convertValue(value.toString());
    }
}
