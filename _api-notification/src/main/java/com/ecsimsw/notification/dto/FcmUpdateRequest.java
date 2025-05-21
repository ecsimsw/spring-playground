package com.ecsimsw.notification.dto;

public record FcmUpdateRequest(
    String username,
    String fcmToken
) {
}
