package com.ecsimsw.error;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public class AuthException extends ApiException {

    public AuthException(ErrorType type) {
        super(type);
    }
}
