package com.hellduo.domain.comment.repository;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.exception.BoardErrorCode;
import com.hellduo.domain.board.exception.BoardException;
import com.hellduo.domain.comment.entity.Comment;
import com.hellduo.domain.comment.exception.CommentErrorCode;
import com.hellduo.domain.comment.exception.CommentException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   List<Comment> findAllByBoard(Board board);

   default Comment findCommentByIdWithThrow(Long id) {
      return findById(id).orElseThrow(()->
              new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));
   };
}
