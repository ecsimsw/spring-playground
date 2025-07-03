package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record HistoryCommonCodes(
    List<String> codes
) {

    public static HistoryCommonCodes of(String... codes) {
        return new HistoryCommonCodes(List.of(codes));
    }

    public boolean contains(String commonCode) {
        return codes.contains(commonCode);
    }
}
