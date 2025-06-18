package com.ecsimsw.common.dto;

public record PairingEventMessage(
    String userId,
    String deviceId
) {
}

