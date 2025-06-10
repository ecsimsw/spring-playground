package com.ecsimsw.springexternalplatform2.dto;

public record Command(
    String code,
    Object value
) {
}