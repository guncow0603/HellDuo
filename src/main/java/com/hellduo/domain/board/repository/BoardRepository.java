package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.exception.BoardErrorCode;
import com.hellduo.domain.board.exception.BoardException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    default Board findBoardByIdWithThrow(Long id) {
        return findById(id).orElseThrow(()->
                new BoardException(BoardErrorCode.NOT_FOUND_BOARD));
    };
}
