package com.ecsimsw.springsdkexternalplatform.domain;

import java.util.List;

public record Product(
    String id,
    PlatformProductType productType,
    PlatformType platformType,
    List<DevicePoint> statusCodes,
    List<DevicePoint> alertCodes
) {

    public String asSpCode(String platformCode) {
        for (var statusCode : statusCodes) {
            if (statusCode.platformCode().equals(platformCode)) {
                return statusCode.spCode();
            }
        }
        for (var alertCode : alertCodes) {
            if (alertCode.platformCode().equals(platformCode)) {
                return alertCode.spCode();
            }
        }
        return platformCode;
    }

    public String asPlatformCode(String spCode) {
        for (var statusCode : statusCodes) {
            if (statusCode.spCode().equals(spCode)) {
                return statusCode.platformCode();
            }
        }
        for (var alertCode : alertCodes) {
            if (alertCode.spCode().equals(spCode)) {
                return alertCode.platformCode();
            }
        }
        return spCode;
    }
}
