package com.hellduo.domain.chat.service;

import com.hellduo.domain.chat.dto.request.ChatMsgReq;
import com.hellduo.domain.chat.dto.response.ChatListRes;
import com.hellduo.domain.chat.dto.response.ChatMsgRes;
import com.hellduo.domain.chat.entity.Chat;
import com.hellduo.domain.chat.repository.ChatRepository;
import com.hellduo.domain.chatRoom.entity.ChatRoom;
import com.hellduo.domain.chatRoom.repository.ChatRoomRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;


    public ChatMsgRes createChat(Long roomId, ChatMsgReq req, StompHeaderAccessor session) {

        ChatRoom chatRoom = findChatRoom(roomId);
        var tempName = session.getSessionAttributes().get("name");
        User sender = findUserByName(tempName.toString());

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(req.message())
                .build();

        chatRepository.save(chat);

        return new ChatMsgRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(),
                chat.getCreatedAt());

    }


    public List<ChatListRes> getChatList(Long roomId) {

        List<Chat> chatList = chatRepository.findAllByChatRoomId(roomId);

        return chatList.stream().map(
                        chat -> new ChatListRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(),
                                chat.getCreatedAt()))
                .toList();
    }


    private ChatRoom findChatRoom(Long roomId) {

        return chatRoomRepository.findById(roomId).orElseThrow
                (() -> new IllegalArgumentException("해당하는 채팅방이 없습니다."));

    }

    private User findUserByName(String name) {
        return userRepository.findByNickname(name)
                .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }
}
