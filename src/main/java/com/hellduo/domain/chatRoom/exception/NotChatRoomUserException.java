package com.hellduo.domain.chatRoom.exception;


import com.hellduo.global.exception.ErrorCode;
import com.hellduo.global.exception.GlobalException;

public class NotChatRoomUserException extends GlobalException {

    public NotChatRoomUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
