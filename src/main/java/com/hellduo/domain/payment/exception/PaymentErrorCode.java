package com.hellduo.domain.payment.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
    NOT_FOUND_ORDER_ID(HttpStatus.NOT_FOUND, "해당 id를 가진 결제 요청이 없습니다."),
    NOT_EQUALS_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액과 요청 결제 금액이 일치하지 않습니다.");
private final HttpStatus httpStatus;
    private final String message;
}

