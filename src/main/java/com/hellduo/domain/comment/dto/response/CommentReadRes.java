package com.hellduo.domain.comment.dto.response;

import com.hellduo.domain.comment.entity.Comment;

public record CommentReadRes(
        String content,
        String userNickname,
        Long commentId
) {
    // Comment 엔티티를 받아서 DTO로 변환하는 생성자
    public CommentReadRes(Comment comment) {
        this(comment.getContent(), comment.getUser().getNickname(), comment.getId());
    }
}
