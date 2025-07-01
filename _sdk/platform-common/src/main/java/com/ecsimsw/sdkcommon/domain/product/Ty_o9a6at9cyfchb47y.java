package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.domain.ProductType;
import com.ecsimsw.sdkcommon.dto.*;

public class Ty_o9a6at9cyfchb47y extends PlatformProduct {

    public Ty_o9a6at9cyfchb47y() {
        super(
            "o9a6at9cyfchb47y",
            ProductType.PRESENCE_SENSOR,
            SupportedCommonCodes.of("add_ele"),
            SupportedPlatformCodes.of("add_ele"),
            StatusCommonCodes.of("add_ele"),
            AlertCommonCodes.of()
        );
    }

    @Override
    public CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus) {
        var platformCode = platformDeviceStatus.code();
        var platformValue = platformDeviceStatus.value();
        if ("add_ele".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, String.class);
            return new CommonDeviceStatus("add_ele", commonValue);
        }
        return new CommonDeviceStatus(platformCode, platformValue);
    }

    @Override
    public PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus) {
        var commonCode = commonDeviceStatus.code();
        var commonValue = commonDeviceStatus.value();
        if ("add_ele".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("add_ele", platformValue);
        }
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
