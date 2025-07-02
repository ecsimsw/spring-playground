package com.ecsimsw.sdkcommon.domain;

import com.ecsimsw.sdkcommon.dto.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PlatformProduct {

    public final String id;
    public final ProductType productType;
    public final SupportedCommonCodes supportedCommonCodes;
    public final SupportedPlatformCodes supportedPlatformCodes;
    public final StatusCommonCodes statusCommonCodes;
    public final AlertCommonCodes alertCommonCodes;
    public final HistoryCommonCodes historyCommonCodes;

    public abstract CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus);

    public abstract PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus);

    public boolean isSupportedPlatformCode(String platformCode) {
        return supportedPlatformCodes.isSupport(platformCode);
    }

    public boolean isSupportedCommonCode(String commonCode) {
        return supportedCommonCodes.isSupport(commonCode);
    }

    public boolean isStatusCode(String commonCode) {
        return statusCommonCodes.contains(commonCode);
    }

    public boolean isAlertCode(String commonCode) {
        return alertCommonCodes.contains(commonCode);
    }

    public boolean isHistoryCode(String commonCode) {
        return historyCommonCodes.contains(commonCode);
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
