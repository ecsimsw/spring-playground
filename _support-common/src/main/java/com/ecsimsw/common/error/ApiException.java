package com.ecsimsw.common.error;

import org.springframework.http.HttpStatus;

public class ApiException extends IllegalArgumentException {

    private static final String MESSAGE_FORM = "%s : %s";
    private final ErrorType type;

    public ApiException(ErrorType type) {
        super(String.format(MESSAGE_FORM, type.getCode().name(), type.getMessage()));
        this.type = type;
    }

    public ApiException(ErrorType type, String message) {
        super(String.format(MESSAGE_FORM, type.getCode().name(), message));
        this.type = type;
    }

    public HttpStatus status() {
        return type.getStatus();
    }

    public String code() {
        return type.getCode().name();
    }

    public String message() {
        return type.getMessage();
    }
}
