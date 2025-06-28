package com.ecsimsw.sdkcommon.dto;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;

public record CommonDeviceStatus(
    String code,
    Object value
) {

    public PlatformDeviceStatus asCommand(PlatformProduct product){
        var devicePoint = product.devicePointFromCommonCode(code);
        var platformCode = devicePoint.platformCode();
        return new PlatformDeviceStatus(platformCode, value);
    }
}