package com.hellduo.domain.admin.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCode
{
    NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND, "공지사항을 찾지 못했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
