package com.ecsimsw.sdkcommon.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlatformProduct {

    private final String id;
    private final List<DevicePoint> statusDevicePoints;
    private final List<DevicePoint> alertDevicePoints;

    public boolean isSupportedPlatformCode(String code) {
        return statusDevicePoints.stream()
            .anyMatch(it -> it.isPlatformCode(code))
            || alertDevicePoints.stream()
            .anyMatch(it -> it.isPlatformCode(code));
    }

    public boolean isSupportedCommonCode(String code) {
        return statusDevicePoints.stream()
            .anyMatch(it -> it.isCommonCode(code))
            || alertDevicePoints.stream()
            .anyMatch(it -> it.isCommonCode(code));
    }

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
}
