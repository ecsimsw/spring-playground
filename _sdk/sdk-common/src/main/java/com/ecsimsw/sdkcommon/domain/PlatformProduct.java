package com.ecsimsw.sdkcommon.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlatformProduct {

    private final String id;
    private final List<DevicePoint> statusCodes;
    private final List<DevicePoint> alertCodes;

    public String asCommonCode(String platformCode) {
        for (var statusCode : statusCodes) {
            if (statusCode.platformCode().equals(platformCode)) {
                return statusCode.commonCode();
            }
        }
        for (var alertCode : alertCodes) {
            if (alertCode.platformCode().equals(platformCode)) {
                return alertCode.commonCode();
            }
        }
        return platformCode;
    }

    public String asPlatformCode(String commonCode) {
        for (var statusCode : statusCodes) {
            if (statusCode.commonCode().equals(commonCode)) {
                return statusCode.platformCode();
            }
        }
        for (var alertCode : alertCodes) {
            if (alertCode.commonCode().equals(commonCode)) {
                return alertCode.platformCode();
            }
        }
        return commonCode;
    }

}
