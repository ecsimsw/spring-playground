package com.ecsimsw.error;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public class UserException extends ApiException {

    public UserException(ErrorType type) {
        super(type);
    }
}
