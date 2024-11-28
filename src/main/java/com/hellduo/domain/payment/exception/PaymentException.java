package com.hellduo.domain.payment.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class PaymentException extends GlobalException {
    public PaymentException(ErrorCode errorCode) { super(errorCode); }
}
