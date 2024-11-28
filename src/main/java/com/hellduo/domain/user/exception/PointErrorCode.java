package com.hellduo.domain.user.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PointErrorCode implements ErrorCode {
    NOT_CHARGE_POINT_LOG(HttpStatus.NOT_FOUND, "충전 포인트 로그가 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
