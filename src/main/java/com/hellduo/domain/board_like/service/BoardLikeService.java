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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final RedissonClient redissonClient; // Redisson 클라이언트

    public LikeResponse boardLikeToggle(Long boardId, User user) {
        // 레디스 락을 위한 키
        String lockKey = "boardLikeLock:" + boardId;

        // 레디스 락 객체
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도
            lock.lock();

            // 게시글 찾기 (예외 처리 포함)
            Board board = boardRepository.findBoardByIdWithThrow(boardId);

            // 좋아요 여부 확인
            Optional<BoardLike> existingLike = boardLikeRepository.findByBoardIdAndUserId(boardId, user.getId());

            if (existingLike.isPresent()) {
                // 이미 좋아요를 눌렀을 경우 좋아요 취소
                board.minusLikeCount(1L);
                boardLikeRepository.delete(existingLike.get());
                return new LikeResponse("좋아요 취소 완료.");
            } else {
                // 좋아요 추가
                board.addLikeCount(1L);
                BoardLike boardLike = BoardLike.builder()
                        .board(board)
                        .user(user)
                        .build();
                boardLikeRepository.save(boardLike);
                return new LikeResponse("좋아요 완료.");
            }
        } finally {
            // 락 해제
            lock.unlock();
        }
    }

}
