package com.ecsimsw.sdkcommon.dto.event;

public record PairingEventMessage(
    String userId,
    String deviceId,
    Long timestamp
) {
}

