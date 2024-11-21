package com.hellduo.domain.board.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class BoardException extends GlobalException {
    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
