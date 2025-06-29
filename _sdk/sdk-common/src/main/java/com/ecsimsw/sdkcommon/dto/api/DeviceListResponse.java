package com.ecsimsw.sdkcommon.dto.api;

import com.ecsimsw.sdkcommon.dto.CommonDeviceStatus;

import java.util.List;

public record DeviceListResponse(
    String id,
    String name,
    String productId,
    boolean online,
    List<CommonDeviceStatus> status
) {
}