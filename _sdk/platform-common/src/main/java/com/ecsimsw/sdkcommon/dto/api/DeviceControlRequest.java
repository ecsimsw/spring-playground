package com.ecsimsw.sdkcommon.dto.api;

import com.ecsimsw.sdkcommon.dto.PlatformDeviceStatus;

import java.util.List;

public record DeviceControlRequest(
    List<PlatformDeviceStatus> platformDeviceStatuses
){
}