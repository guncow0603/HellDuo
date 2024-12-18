package com.hellduo.domain.chatRoom.entity;

import com.hellduo.domain.common.BaseEntity;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_CHATROOM")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Builder
    private ChatRoom(String name, User sender, User receiver) {
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
    }
}