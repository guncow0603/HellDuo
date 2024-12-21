package com.hellduo.domain.board.repository;

import com.hellduo.domain.board.entity.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select p from Board p where" +
            "(:keyword is null or p.title like %:keyword%)")
    List<Board> searchByKeyword(@Param("keyword") String keyword);

    List<Board> findTop10ByOrderByLikeCountDesc();

    // Board와 연관된 commentList, boardLikeList를 함께 조회
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.commentList WHERE b.id = :boardId")
    Board findBoardByIdWithThrow(Long boardId);
}
