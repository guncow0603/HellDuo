package com.hellduo.domain.board.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode{
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    BOARD_CURRENT_USER(HttpStatus.NOT_FOUND, "잘못된 접근입니다. 게시글 작성자와 로그인 정보가 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
