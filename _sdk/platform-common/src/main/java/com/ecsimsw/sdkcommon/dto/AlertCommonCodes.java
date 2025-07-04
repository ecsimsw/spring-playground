package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record AlertCommonCodes(
    List<AlertCommonCode> codes
) {

    public static AlertCommonCodes of(AlertCommonCode... codes) {
        return new AlertCommonCodes(List.of(codes));
    }

    public boolean contains(String commonCode) {
        return codes.stream().anyMatch(it -> it.commonCode().equals(commonCode));
    }

    public AlertCommonCode fromCommonCode(String commonCode) {
        return codes.stream()
            .filter(it -> it.commonCode().equals(commonCode))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Not a supported alert code"));
    }
}
