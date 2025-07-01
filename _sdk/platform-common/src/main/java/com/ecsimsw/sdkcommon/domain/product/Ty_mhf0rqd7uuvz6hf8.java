package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.domain.ProductType;
import com.ecsimsw.sdkcommon.dto.*;
import com.ecsimsw.sdkcommon.support.StatusValueParseUtils;

public class Ty_mhf0rqd7uuvz6hf8 extends PlatformProduct {

    public Ty_mhf0rqd7uuvz6hf8() {
        super(
            "mhf0rqd7uuvz6hf8",
            ProductType.BLUNT,
            SupportedPlatformCodes.of("switch_led", "bright_value", "work_mode"),
            StatusCommonCodes.of("switch", "bright", "mode"),
            AlertCommonCodes.of()
        );
    }

    @Override
    public CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus) {
        var platformCode = platformDeviceStatus.code();
        var platformValue = platformDeviceStatus.value();
        if ("switch_led".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, Boolean.class);
            return new CommonDeviceStatus("switch", commonValue);
        }
        if ("bright_value".equals(platformCode)) {
            var intValue = (Integer) convertValueType(platformValue, Integer.class);
            var commonValue = StatusValueParseUtils.mapRange(intValue, 27, 255, 1, 100);
            return new CommonDeviceStatus("bright", commonValue);
        }
        if ("work_mode".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, String.class);
            return new CommonDeviceStatus("mode", commonValue);
        }
        return new CommonDeviceStatus(platformCode, platformValue);
    }

    @Override
    public PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus) {
        var commonCode = commonDeviceStatus.code();
        var commonValue = commonDeviceStatus.value();
        if ("switch".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, Boolean.class);
            return new PlatformDeviceStatus("switch_led", platformValue);
        }
        if ("bright".equals(commonCode)) {
            var intValue = (Integer) convertValueType(commonValue, Integer.class);
            var platformValue = StatusValueParseUtils.mapRange(intValue, 1, 100, 27, 255);
            return new PlatformDeviceStatus("bright_value", platformValue);
        }
        if ("mode".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("work_mode", platformValue);
        }
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
