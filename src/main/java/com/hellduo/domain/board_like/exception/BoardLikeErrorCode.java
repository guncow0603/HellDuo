package com.hellduo.domain.board_like.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardLikeErrorCode implements ErrorCode {
    DUPLICATE_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
