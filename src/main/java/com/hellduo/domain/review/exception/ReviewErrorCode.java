package com.hellduo.domain.review.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    PT_REVIEW_ALREADY_WRITTEN(HttpStatus.BAD_REQUEST, "이미 리뷰가 작성된 PT입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
