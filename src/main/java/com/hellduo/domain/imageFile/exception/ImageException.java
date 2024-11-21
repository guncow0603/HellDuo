package com.hellduo.domain.imageFile.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class ImageException extends GlobalException {
    public ImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
