package com.ecsimsw.account.error;

import com.ecsimsw.common.error.ApiException;
import com.ecsimsw.common.error.ErrorType;

public class AccountException extends ApiException {

    public AccountException(ErrorType type) {
        super(type);
    }
}
