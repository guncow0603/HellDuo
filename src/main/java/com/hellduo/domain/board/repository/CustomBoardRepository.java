package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.dto.response.BoardsReadRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoardRepository {

    Page<BoardsReadRes> searchBoards(Pageable pageable, String keyword);
}
