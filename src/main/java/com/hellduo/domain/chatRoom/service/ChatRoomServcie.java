package com.hellduo.domain.chatRoom.service;

import com.hellduo.domain.chatRoom.dto.ChatRoomCreateReq;
import com.hellduo.domain.chatRoom.dto.ChatRoomCreateRes;
import com.hellduo.domain.chatRoom.dto.ChatRoomDeleteRes;
import com.hellduo.domain.chatRoom.dto.ChatRoomListRes;
import com.hellduo.domain.chatRoom.entity.ChatRoom;
import com.hellduo.domain.chatRoom.exception.ChatRoomErrorCode;
import com.hellduo.domain.chatRoom.exception.NotChatRoomUserException;
import com.hellduo.domain.chatRoom.repository.ChatRoomRepository;
import com.hellduo.domain.user.dto.response.UserRes;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomServcie {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;


    public ChatRoomCreateRes createChatRoom(Long userId, ChatRoomCreateReq req) {
        User receiver = userRepository.findUserByIdWithThrow(req.receiverId());
        User sender = userRepository.findUserByIdWithThrow(userId);

        //receiver sender를 구별되지않게하기위함 (단순 DB저장용)
        if (chatRoomRepository.existsByReceiverIdAndSenderId(receiver.getId(), sender.getId())) {
            ChatRoom rm = chatRoomRepository.findByReceiverIdAndSenderId(receiver.getId(),
                sender.getId()).get();
            User send = rm.getSender();
            User recei = rm.getReceiver();
            UserRes sender2 = new UserRes(send.getId(), send.getNickname());
            UserRes receiver2 = new UserRes(recei.getId(), recei.getNickname());

            return new ChatRoomCreateRes(rm.getId(), sender2, receiver2);
        }

        if (chatRoomRepository.existsByReceiverIdAndSenderId(sender.getId(), receiver.getId())) {
            ChatRoom rm = chatRoomRepository.findByReceiverIdAndSenderId(sender.getId(),
                receiver.getId()).get();

            User send = rm.getSender();
            User recei = rm.getReceiver();
            UserRes sender2 = new UserRes(send.getId(), send.getNickname());
            UserRes receiver2 = new UserRes(recei.getId(), recei.getNickname());

            return new ChatRoomCreateRes(rm.getId(), receiver2, sender2);
        }

        ChatRoom room = ChatRoom.builder()
            .name("채팅방")
            .sender(sender)
            .receiver(receiver)
            .build();

        chatRoomRepository.save(room);

        User send = room.getSender();
        User recei = room.getReceiver();
        UserRes sender2 = new UserRes(send.getId(), send.getNickname());
        UserRes receiver2 = new UserRes(recei.getId(), recei.getNickname());

        return new ChatRoomCreateRes(room.getId(), receiver2, sender2);

    }


    public ChatRoomDeleteRes deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdWithThrow(roomId);
        if (!(chatRoom.getReceiver().getId().equals(userId) || chatRoom.getSender().getId()
            .equals(userId))) {
            //채팅방 참여자일경우만 채팅방 나가기 가능
            throw new NotChatRoomUserException(ChatRoomErrorCode.NOT_CHATROOM_USER);
        }

        chatRoomRepository.delete(chatRoom);

        return new ChatRoomDeleteRes("삭제가 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<ChatRoomListRes> getChatRoomList(Long userId) {
        List<ChatRoom> res = chatRoomRepository.findAllByReceiverIdOrSenderId(userId, userId);

        return res.stream().map(
            chatRoom -> {
                return new ChatRoomListRes(chatRoom.getId(), chatRoom.getSender().getNickname(),
                    chatRoom.getReceiver().getNickname());
            }
        ).toList();

    }
}
