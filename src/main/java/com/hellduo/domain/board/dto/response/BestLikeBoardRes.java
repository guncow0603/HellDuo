package com.hellduo.domain.board.dto.response;

public record BestLikeBoardRes(
        Long boardId,
        Long boardLikeCount,
        String title,
        String content
) {

}
