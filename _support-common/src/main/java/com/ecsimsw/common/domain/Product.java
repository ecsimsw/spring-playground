package com.ecsimsw.common.domain;

public record Product(
    String id,
    ProductType type
) {

    public boolean isStatusCode(String code) {
        return type.statusCodes.stream()
            .anyMatch(it -> it.name().equals(code));
    }

    public boolean isAlertCode(String code) {
        return type.alertCodes.stream()
            .anyMatch(it -> it.name().equals(code));
    }

    public Object parseValue(String code, Object value) {
        for (var statusCode : type.statusCodes) {
            if (statusCode.name().equals(code)) {
                return statusCode.convertValue(value);
            }
        }
        for (var alertCode : type.alertCodes) {
            if (alertCode.name().equals(code)) {
                return alertCode.convertValue(value);
            }
        }
        return value;
    }

    public String parseCode(String originCode) {
        for (var statusCode : type.statusCodes) {
            if (statusCode.originName().equals(originCode)) {
                return statusCode.name();
            }
        }
        for (var alertCode : type.alertCodes) {
            if (alertCode.originName().equals(originCode)) {
                return alertCode.name();
            }
        }
        return originCode;
    }
}
