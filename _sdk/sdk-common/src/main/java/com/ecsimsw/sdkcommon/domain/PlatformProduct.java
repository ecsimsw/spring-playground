package com.ecsimsw.sdkcommon.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlatformProduct {

    private final String id;
    private final List<DevicePoint> statusDevicePoints;
    private final List<DevicePoint> alertDevicePoints;

    public DevicePoint devicePointFromPlatformCode(String code) {
        for (var statusCode : statusDevicePoints) {
            if (statusCode.isPlatformCode(code)) {
                return statusCode;
            }
        }
        for (var alertCode : alertDevicePoints) {
            if (alertCode.isPlatformCode(code)) {
                return alertCode;
            }
        }
        throw new IllegalArgumentException("No device point found for platform code: " + code);
    }

    public DevicePoint devicePointFromCommonCode(String code) {
        for (var statusCode : statusDevicePoints) {
            if (statusCode.isCommonCode(code)) {
                return statusCode;
            }
        }
        for (var alertCode : alertDevicePoints) {
            if (alertCode.isCommonCode(code)) {
                return alertCode;
            }
        }
        throw new IllegalArgumentException("No device point found for platform code: " + code);
    }

    public String asCommonCode(String platformCode) {
        for (var statusCode : statusDevicePoints) {
            if (statusCode.platformCode().equals(platformCode)) {
                return statusCode.commonCode();
            }
        }
        for (var alertCode : alertDevicePoints) {
            if (alertCode.platformCode().equals(platformCode)) {
                return alertCode.commonCode();
            }
        }
        return platformCode;
    }

    public String asPlatformCode(String commonCode) {
        for (var statusCode : statusDevicePoints) {
            if (statusCode.commonCode().equals(commonCode)) {
                return statusCode.platformCode();
            }
        }
        for (var alertCode : alertDevicePoints) {
            if (alertCode.commonCode().equals(commonCode)) {
                return alertCode.platformCode();
            }
        }
        return commonCode;
    }

}
