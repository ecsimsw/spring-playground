package com.ecsimsw.sdkcommon.domain;

public record DevicePoint(
    String platformCode,
    String commonCode
) {

    public boolean isPlatformCode(String platformCode) {
        return this.platformCode.equalsIgnoreCase(platformCode);
    }

    public boolean isCommonCode(String commonCode) {
        return this.commonCode.equalsIgnoreCase(commonCode);
    }
}
