package com.hellduo.domain.comment.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class CommentException extends GlobalException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
