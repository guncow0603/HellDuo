package com.hellduo.domain.review.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class ReviewException extends GlobalException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
