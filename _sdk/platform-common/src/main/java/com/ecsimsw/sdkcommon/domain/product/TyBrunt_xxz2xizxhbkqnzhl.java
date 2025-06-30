package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.PlatformDeviceStatus;

import java.util.List;

public class TyBrunt_xxz2xizxhbkqnzhl extends PlatformProduct {

    public TyBrunt_xxz2xizxhbkqnzhl() {
        super(
            "xxz2xizxhbkqnzhl",
            List.of("switch_led", "bright_value", "work_mode"),
            List.of("switch", "bright", "mode"),
            List.of()
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
            var commonValue = convertValueType(platformValue, Integer.class);
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
            var platformValue = convertValueType(commonValue, Integer.class);
            return new PlatformDeviceStatus("bright_value", platformValue);
        }
        if ("mode".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("work_mode", platformValue);
        }
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
