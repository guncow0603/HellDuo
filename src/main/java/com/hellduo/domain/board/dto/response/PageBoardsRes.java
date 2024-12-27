package com.hellduo.domain.board.dto.response;

import java.util.List;

public record PageBoardsRes(
        List<BoardsReadRes> content,   // 게시물 목록
        int totalPages,                // 전체 페이지 수
        int number                    // 현재 페이지 번호
) {
}
