package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record AlertCommonCodes(
    List<String> codes
) {

    public static AlertCommonCodes of(String... codes) {
        return new AlertCommonCodes(List.of(codes));
    }

    public boolean contains(String commonCode) {
        return codes.contains(commonCode);
    }
}
