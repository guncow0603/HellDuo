package com.hellduo.domain.comment.dto.response;

public record CommentReadRes(
        String content,
        String userNickname
) {
}
