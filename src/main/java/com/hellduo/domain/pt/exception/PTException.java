package com.hellduo.domain.pt.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class PTException extends GlobalException {
    public PTException(ErrorCode errorCode) {
        super(errorCode);
    }
}
