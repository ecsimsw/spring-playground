package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record HistoryCommonCodes(
    List<HistoryCodeConfigs> codes
) {

    public static HistoryCommonCodes of(HistoryCodeConfigs... codes) {
        return new HistoryCommonCodes(List.of(codes));
    }

    public boolean contains(String commonCode) {
        return codes.stream().anyMatch(it -> it.commonCode().equals(commonCode));
    }
}
