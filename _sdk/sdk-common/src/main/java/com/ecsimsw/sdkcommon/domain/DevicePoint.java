package com.ecsimsw.sdkcommon.domain;

import java.util.function.Function;

public record DevicePoint(
    String platformCode,
    String commonCode,
    Function<Object, Object> parseToPlatformValue,
    Function<Object, Object> parseToCommonValue
) {

    public DevicePoint(String platformCode, String commonCode) {
        this(platformCode, commonCode, null, null);
    }

    public boolean isPlatformCode(String platformCode) {
        return this.platformCode.equalsIgnoreCase(platformCode);
    }

    public boolean isCommonCode(String commonCode) {
        return this.commonCode.equalsIgnoreCase(commonCode);
    }

    public Object parseToPlatformValue(Object commonValue) {
        if(parseToPlatformValue == null) {
            return commonValue;
        }
        return parseToCommonValue.apply(commonValue);
    }

    public Object parseToCommonValue(Object platformValue) {
        if(parseToPlatformValue == null) {
            return platformValue;
        }
        return parseToPlatformValue.apply(platformValue);
    }
}
