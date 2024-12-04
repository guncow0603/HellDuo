package com.hellduo.domain.chatRoom.repository;


import com.hellduo.domain.chatRoom.entity.ChatRoom;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Boolean existsByReceiverIdAndSenderId(Long receiverId, Long SenderId);

    Optional<ChatRoom> findByReceiverIdAndSenderId(Long receiverId, Long SenderId);

    List<ChatRoom> findAllByReceiverIdOrSenderId(Long receiverId, Long SenderId);

    default ChatRoom findChatRoomByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new PTException(PTErrorCode.NOT_TRAiNER));
    }

}
