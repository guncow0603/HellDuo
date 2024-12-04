package com.hellduo.domain.chat.repository;

import com.hellduo.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findAllByChatRoomId(Long roomId);
}
