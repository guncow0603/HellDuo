package com.hellduo.domain.admin.exception;

import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class AdminException extends GlobalException
{
    public AdminException(ErrorCode errorCode)
    {
        super(errorCode);
    }
}
