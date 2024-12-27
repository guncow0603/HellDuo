package com.hellduo.domain.board.dto.response;

import com.hellduo.domain.comment.dto.response.CommentReadRes;

import java.util.List;

public record BoardReadRes(
        Long boardId,
        Long boardLikeCount,
        String title,
        String content,
        Long userId,
        List<CommentReadRes> commentList
) {}
