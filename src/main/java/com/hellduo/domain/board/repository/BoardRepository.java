package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.exception.BoardErrorCode;
import com.hellduo.domain.board.exception.BoardException;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    default Board findBoardByIdWithThrow(Long id) {
        return findById(id).orElseThrow(()->
                new BoardException(BoardErrorCode.NOT_FOUND_BOARD));
    };

    @Query("select p from Board p where" +
            "(:keyword is null or p.title like %:keyword%)")
    List<Board> searchByKeyword(@Param("keyword") String keyword);

    List<Board> findTop10ByOrderByLikeCountDesc();
}
