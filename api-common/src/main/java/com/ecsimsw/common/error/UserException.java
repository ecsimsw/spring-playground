package com.ecsimsw.common.error;

public class UserException extends ApiException {

    public UserException(ErrorType type) {
        super(type);
    }
}
