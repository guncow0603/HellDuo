package com.hellduo.domain.board_like.repository;

import com.hellduo.domain.board_like.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    void deleteBoardLikesByBoardIdAndUserId(Long boardId, Long userid);
}