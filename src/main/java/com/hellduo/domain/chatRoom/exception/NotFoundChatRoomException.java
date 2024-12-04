package com.hellduo.domain.chatRoom.exception;


import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class NotFoundChatRoomException extends GlobalException {

    public NotFoundChatRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}

