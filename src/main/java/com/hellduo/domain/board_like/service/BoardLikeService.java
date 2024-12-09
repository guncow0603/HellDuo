package com.hellduo.domain.board_like.service;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.board_like.dto.response.LikeResponse;
import com.hellduo.domain.board_like.entity.BoardLike;
import com.hellduo.domain.board_like.exception.BoardLikeErrorCode;
import com.hellduo.domain.board_like.exception.BoardLikeException;
import com.hellduo.domain.board_like.repository.BoardLikeRepository;
import com.hellduo.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    public LikeResponse boardLike(Long boardId, User user) {
        Board board =  boardRepository.findBoardByIdWithThrow(boardId);
        board.addLikeCount(1L);
        if(boardLikeRepository.existsByBoardIdAndUserId(boardId, user.getId())) {
            throw new BoardLikeException(BoardLikeErrorCode.DUPLICATE_LIKE);
        }
        BoardLike boardLike = BoardLike.builder().
                board(board).
                user(user).
                build();
        boardLikeRepository.save(boardLike);
        return new LikeResponse("좋아요 완료.");
    }

    public LikeResponse boardLikeDelete(Long boardId, User user) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId);
        board.minusLikeCount(1L);
        boardLikeRepository.deleteBoardLikesByBoardIdAndUserId(boardId, user.getId());
        return new LikeResponse("좋아요 취소 완료,");
    }
}
