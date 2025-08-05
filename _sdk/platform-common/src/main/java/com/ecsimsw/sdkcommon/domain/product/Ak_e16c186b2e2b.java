package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.domain.ProductType;
import com.ecsimsw.sdkcommon.dto.*;

public class Ak_e16c186b2e2b extends PlatformProduct {

    public Ak_e16c186b2e2b() {
        super(
            "e16c186b2e2b",
            ProductType.INTERCOM,
            SupportedCommonCodes.of("doorLog", "motionDetected"),
            SupportedPlatformCodes.of("doorLog", "motionDetected"),
            StatusCommonCodes.of(),
            AlertCommonCodes.of(),
            HistoryCommonCodes.of("doorLog", "motionDetected")
        );
    }

    @Override
    public CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus) {
        var platformCode = platformDeviceStatus.code();
        var platformValue = platformDeviceStatus.value();
        return new CommonDeviceStatus(platformCode, platformValue);
    }

    @Override
    public PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus) {
        var commonCode = commonDeviceStatus.code();
        var commonValue = commonDeviceStatus.value();
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
