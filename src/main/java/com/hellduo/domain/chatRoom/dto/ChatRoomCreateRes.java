package com.hellduo.domain.chatRoom.dto;


import com.hellduo.domain.user.dto.response.UserRes;

public record ChatRoomCreateRes(Long id, UserRes sender, UserRes receiver) {

}
