package com.ecsimsw.common.error;

public class AuthException extends ApiException {

    public AuthException(ErrorType type) {
        super(type);
    }
}
