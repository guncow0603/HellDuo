package com.hellduo.domain.chatRoom.controller;

import com.hellduo.domain.chatRoom.dto.ChatRoomCreateReq;
import com.hellduo.domain.chatRoom.dto.ChatRoomCreateRes;
import com.hellduo.domain.chatRoom.dto.ChatRoomDeleteRes;
import com.hellduo.domain.chatRoom.dto.ChatRoomListRes;
import com.hellduo.domain.chatRoom.service.ChatRoomServcie;
import com.hellduo.domain.notify.NotificationService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomServcie chatRoomServcie;
    private final NotificationService notificationService;

    @GetMapping
    public List<ChatRoomListRes> getChatRoomList
        (@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomServcie.getChatRoomList(userDetails.getUser().getId());

    }

    @PostMapping
    public ChatRoomCreateRes createChatRoom
        (@RequestBody ChatRoomCreateReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        var b = chatRoomServcie.createChatRoom(userDetails.getUser().getId(), req);
        notificationService.notifyMessage(req.receiverId());
        return b;

    }

    @DeleteMapping("/{roomId}")
    public ChatRoomDeleteRes deleteChatRoom
        (@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomServcie.deleteChatRoom(roomId, userDetails.getUser().getId());

    }

}
