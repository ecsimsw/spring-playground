package com.ecsimsw.notification.dto;

public record FcmMessageRequest(
    String targetToken,
    String title,
    String body
) {
}
