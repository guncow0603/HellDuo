package com.hellduo.domain.imageFile.entity;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_board_image")
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("S3 URL")
    @Column(name = "board_image_url", nullable = false)
    private String boardImageUrl;

    @Comment("유저 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("보드 id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    private BoardImage(String boardImageUrl, User user,Board board) {
        this.boardImageUrl = boardImageUrl;
        this.user = user;
        this.board=board;
    }
}
