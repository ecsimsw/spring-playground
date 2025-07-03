package com.ecsimsw.device.dto;

import java.util.List;

public record DeviceHistoryPageResponse(
    List<DeviceHistoryResponse> contents,
    Long totalCount,
    PageInfo pageInfo
) {
}
