package com.hellduo.domain.comment.entity;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Entity(name="tb_comment")
@Getter
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }
    public void updateContent(String content) {
        this.content = content;
    }
}
