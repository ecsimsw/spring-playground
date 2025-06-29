package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.PlatformDeviceStatus;

import java.util.List;

public class HomeCameraProPlus extends PlatformProduct {

    public HomeCameraProPlus() {
        super(
            "3cwbcqiz8qixphvu",
            List.of("basic_indicator", "basic_private", "motion_switch"),
            List.of("indicator", "privateMode", "motionDetect"),
            List.of("linkage")
        );
    }

    @Override
    public CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus) {
        var platformCode = platformDeviceStatus.code();
        var platformValue = platformDeviceStatus.value();
        if ("basic_indicator".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, Boolean.class);
            return new CommonDeviceStatus("indicator", commonValue);
        }
        if ("basic_private".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, Boolean.class);
            return new CommonDeviceStatus("privateMode", commonValue);
        }
        if ("motion_switch".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, Boolean.class);
            return new CommonDeviceStatus("motionDetect", commonValue);
        }
        if ("linkage".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, String.class);
            return new CommonDeviceStatus("linkage", commonValue);
        }
        return new CommonDeviceStatus(platformCode, platformValue);
    }

    @Override
    public PlatformDeviceStatus fromCommonStatus(CommonDeviceStatus commonDeviceStatus) {
        var commonCode = commonDeviceStatus.code();
        var commonValue = commonDeviceStatus.value();
        if ("indicator".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, Boolean.class);
            return new PlatformDeviceStatus("basic_indicator", platformValue);
        }
        if ("privateMode".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, Integer.class);
            return new PlatformDeviceStatus("basic_private", platformValue);
        }
        if ("motionDetect".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("motion_switch", platformValue);
        }
        if ("linkage".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("linkage", platformValue);
        }
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
