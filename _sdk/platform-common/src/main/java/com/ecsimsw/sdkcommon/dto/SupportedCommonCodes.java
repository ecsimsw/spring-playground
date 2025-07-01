package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record SupportedCommonCodes(
    List<String> codes
) {

    public static SupportedCommonCodes of(String... codes) {
        return new SupportedCommonCodes(List.of(codes));
    }

    public boolean isSupport(String commonCodes) {
        return codes.contains(commonCodes);
    }
}
