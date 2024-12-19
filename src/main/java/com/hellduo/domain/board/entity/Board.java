package com.hellduo.domain.board.entity;

import com.hellduo.domain.board_like.entity.BoardLike;
import com.hellduo.domain.comment.entity.Comment;
import com.hellduo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "TB_Board")
@RequiredArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column(name = "likeCount", nullable = false)
    @ColumnDefault("0")
    private Long likeCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Board 삭제 시 관련된 댓글도 함께 삭제되도록 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    // Board 삭제 시 관련된 좋아요도 함께 삭제되도록 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();


    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addLikeCount(Long likeCount) {this.likeCount += likeCount; }

    public void minusLikeCount(Long likeCount) {this.likeCount -= likeCount; }
}