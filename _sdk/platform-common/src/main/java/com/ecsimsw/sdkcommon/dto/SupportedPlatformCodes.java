package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record SupportedPlatformCodes(
    List<String> codes
) {

    public static SupportedPlatformCodes of(String... codes) {
        return new SupportedPlatformCodes(List.of(codes));
    }

    public boolean isSupport(String platformCode) {
        return codes.contains(platformCode);
    }
}
