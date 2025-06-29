package com.ecsimsw.sdkcommon.domain;

import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.PlatformDeviceStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class PlatformProduct {

    public final String id;
    public final List<String> statusPlatformCode;
    public final List<String> alertPlatformCode;

    public abstract CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus);

    public abstract PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus);

    public boolean isSupportedPlatformCode(String code) {
        return statusPlatformCode.stream().anyMatch(it -> it.equals(code))
            || alertPlatformCode.stream().anyMatch(it -> it.equals(code));
    }

    protected Object convertValueType(Object value, Class<?> type) {
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return value;
        }
        if (value instanceof String string) {
            if (type == Integer.class) {
                return Integer.parseInt(string);
            }
            if (type == Float.class) {
                return Float.parseFloat(string);
            }
            if (type == Double.class) {
                return Double.parseDouble(string);
            }
            if (type == Boolean.class) {
                return Boolean.parseBoolean(string);
            }
            return string;
        }
        return convertValueType(value.toString(), type);
    }
}
