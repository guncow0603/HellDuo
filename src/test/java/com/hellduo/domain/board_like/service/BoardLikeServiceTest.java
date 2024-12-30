package com.hellduo.domain.board_like.service;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.board_like.dto.response.LikeResponse;
import com.hellduo.domain.board_like.entity.BoardLike;
import com.hellduo.domain.board_like.repository.BoardLikeRepository;
import com.hellduo.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BoardLikeServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardLikeRepository boardLikeRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    private BoardLikeService boardLikeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boardLikeService = new BoardLikeService(boardRepository, boardLikeRepository, redissonClient);
    }

    @Test
    void boardLikeToggle_shouldCancelLike_whenAlreadyLiked() {
        // Given
        Long boardId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board = new Board();
        board.setId(boardId);
        board.setLikeCount(1L);

        BoardLike boardLike = BoardLike.builder().board(board).user(user).build();

        when(redissonClient.getLock("boardLikeLock:" + boardId)).thenReturn(lock);
        when(boardRepository.findBoardByIdWithThrow(boardId)).thenReturn(board);
        when(boardLikeRepository.findByBoardIdAndUserId(boardId, user.getId())).thenReturn(Optional.of(boardLike));

        // When
        LikeResponse response = boardLikeService.boardLikeToggle(boardId, user);

        // Then
        assertEquals("좋아요 취소 완료.", response.msg());
        assertEquals(0L, board.getLikeCount());
        verify(boardLikeRepository, times(1)).delete(boardLike);
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
    }

    @Test
    void boardLikeToggle_shouldAddLike_whenNotLiked() {
        // Given
        Long boardId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board = new Board();
        board.setId(boardId);
        board.setLikeCount(0L);

        when(redissonClient.getLock("boardLikeLock:" + boardId)).thenReturn(lock);
        when(boardRepository.findBoardByIdWithThrow(boardId)).thenReturn(board);
        when(boardLikeRepository.findByBoardIdAndUserId(boardId, user.getId())).thenReturn(Optional.empty());

        // When
        LikeResponse response = boardLikeService.boardLikeToggle(boardId, user);

        // Then
        assertEquals("좋아요 완료.", response.msg());
        assertEquals(1L, board.getLikeCount());
        verify(boardLikeRepository, times(1)).save(any(BoardLike.class));
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
    }
}
