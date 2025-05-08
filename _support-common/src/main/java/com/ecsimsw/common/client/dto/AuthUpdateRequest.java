package com.ecsimsw.common.client.dto;

public record AuthUpdateRequest(
    String username,
    String newPassword
) {
}
