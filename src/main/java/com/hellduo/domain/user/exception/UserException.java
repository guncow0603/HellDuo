package com.hellduo.domain.user.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class UserException extends GlobalException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
