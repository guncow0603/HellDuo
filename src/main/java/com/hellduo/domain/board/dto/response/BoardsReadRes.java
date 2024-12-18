package com.hellduo.domain.board.dto.response;

public record BoardsReadRes(
        Long boardId,
        String title,
        Long boardLikeCount
) {
}
