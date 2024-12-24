package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> , CustomBoardRepository{

    List<Board> findTop10ByOrderByLikeCountDesc();

    // Board와 연관된 commentList, boardLikeList를 함께 조회
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.commentList WHERE b.id = :boardId")
    Board findBoardByIdWithThrow(Long boardId);
}
