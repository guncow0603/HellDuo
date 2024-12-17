package com.hellduo.domain.comment.exception;

import com.hellduo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "내용이 비어 있습니다."),
    COMMENT_CURRENT_USER(HttpStatus.NOT_FOUND, "잘못된 접근입니다. 댓글 작성자와 로그인 정보가 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
