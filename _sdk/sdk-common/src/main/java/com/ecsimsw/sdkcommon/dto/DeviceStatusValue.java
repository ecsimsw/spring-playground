package com.ecsimsw.sdkcommon.dto;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;

public record DeviceStatusValue(
    String code,
    Object value
) {

    public DeviceCommand asCommand(PlatformProduct product){
        var platformCode = product.asPlatformCode(code);
        return new DeviceCommand(platformCode, value);
    }
}