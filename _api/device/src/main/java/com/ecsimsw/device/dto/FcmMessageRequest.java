package com.ecsimsw.device.dto;

public record FcmMessageRequest(
    String targetToken,
    String title,
    String body
) {
}
