package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record StatusCommonCodes(
    List<String> codes
) {

    public static StatusCommonCodes of(String... codes) {
        return new StatusCommonCodes(List.of(codes));
    }

    public boolean contains(String commonCode) {
        return codes.contains(commonCode);
    }
}
