package com.hellduo.domain.user.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class PointException extends GlobalException {
    public PointException(ErrorCode errorCode) {
        super(errorCode);
    }
}
