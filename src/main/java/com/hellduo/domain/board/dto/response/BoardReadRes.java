package com.hellduo.domain.board.dto.response;

public record BoardReadRes(
        Long boardId,
        Long boardLikeCount,
        String title,
        String content,
        Long userId
){
    
}
