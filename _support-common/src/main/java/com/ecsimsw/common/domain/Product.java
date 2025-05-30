package com.ecsimsw.common.domain;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public record Product(
    String id,
    ProductType type
) {

    public boolean isSupportedStatusCode(String statusCode) {
        return type.statusCodes.stream()
            .anyMatch(it -> it.name().equals(statusCode));
    }

    public Object convertValue(String statusCode, Object value) {
        return type.statusCodes.stream()
            .filter(it -> it.name().equals(statusCode))
            .findAny()
            .orElseThrow(() -> new ApiException(ErrorType.NOT_SUPPORTED_DEVICE))
            .convertValue(value);
    }
}
