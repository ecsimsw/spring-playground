package com.ecsimsw.sdkcommon.domain.product;

import com.ecsimsw.sdkcommon.domain.PlatformProduct;
import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;
import com.ecsimsw.sdkcommon.dto.PlatformDeviceStatus;

import java.util.List;

public class TyPlugMini_uxjr57hvapakd0io extends PlatformProduct {

    public TyPlugMini_uxjr57hvapakd0io() {
        super(
            "uxjr57hvapakd0io",
            List.of("switch_1", "cur_current", "cur_power", "cur_voltage"),
            List.of("switch", "cur_current", "cur_power", "cur_voltage"),
            List.of()
        );
    }

    @Override
    public CommonDeviceStatus toCommonStatus(PlatformDeviceStatus platformDeviceStatus) {
        var platformCode = platformDeviceStatus.code();
        var platformValue = platformDeviceStatus.value();
        if ("switch_1".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, Boolean.class);
            return new CommonDeviceStatus("switch", commonValue);
        }
        if ("cur_current".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, String.class);
            return new CommonDeviceStatus("cur_current", commonValue);
        }
        if ("cur_power".equals(platformCode)) {
            var commonValue = convertValueType(platformValue, String.class);
            return new CommonDeviceStatus("cur_power", commonValue);
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
        if ("cur_current".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("cur_current", platformValue);
        }
        if ("cur_power".equals(commonCode)) {
            var platformValue = convertValueType(commonValue, String.class);
            return new PlatformDeviceStatus("cur_power", platformValue);
        }
        return new PlatformDeviceStatus(commonCode, commonValue);
    }
}
