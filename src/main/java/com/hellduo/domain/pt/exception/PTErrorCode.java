package com.hellduo.domain.pt.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum PTErrorCode implements ErrorCode {
    PT_NOT_FOUND(HttpStatus.NOT_FOUND, "PT를 찾을수 없습니다."),
    NOT_TRAiNER(HttpStatus.BAD_REQUEST, "트레이너만 등록 가능합니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
