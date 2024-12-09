package com.hellduo.domain.board_like.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class BoardLikeException extends GlobalException {
    public BoardLikeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
