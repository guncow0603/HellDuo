package com.hellduo.domain.comment.repository;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   List<Comment> findAllByBoard(Board board);
}
