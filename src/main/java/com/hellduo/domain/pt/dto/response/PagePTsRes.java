package com.hellduo.domain.pt.dto.response;

import com.hellduo.domain.board.dto.response.BoardsReadRes;

import java.util.List;

public record PagePTsRes(
        List<PTsReadRes> content,   // 게시물 목록
        int totalPages,                // 전체 페이지 수
        int number                    // 현재 페이지 번호
) {
}
