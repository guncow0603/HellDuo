package com.hellduo.domain.comment.dto.request;

public record CommentCreatReq(
        String content,
        Long boardId
) {
}
