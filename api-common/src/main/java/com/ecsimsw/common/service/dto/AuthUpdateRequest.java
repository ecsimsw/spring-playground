package com.ecsimsw.common.service.dto;

public record AuthUpdateRequest(
    String username,
    String newPassword
) {
}
