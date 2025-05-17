package com.ecsimsw.common.service.client.dto;

public record AuthUpdateRequest(
    String username,
    String newPassword
) {
}
