package com.ecsimsw.common.dto;

public record Command(
    String code,
    Object value
) {
}