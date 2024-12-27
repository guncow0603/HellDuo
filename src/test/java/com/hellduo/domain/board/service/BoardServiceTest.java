package com.hellduo.domain.board.service;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.request.BoardUpdateReq;
import com.hellduo.domain.board.dto.response.*;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.exception.BoardErrorCode;
import com.hellduo.domain.board.exception.BoardException;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.comment.entity.Comment;
import com.hellduo.domain.imageFile.service.ImageFileService;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ImageFileService imageFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, String email, UserRoleType role) {
        User user = User.builder()
                .email(email)
                .role(role)
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Board createBoard(Long id, String title, String content, User user) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        ReflectionTestUtils.setField(board, "id", id);
        return board;
    }

    @Test
    void createBoard_shouldCreateBoardSuccessfully() {
        // Given
        User user = createUser(1L, "user@example.com", UserRoleType.USER);
        BoardCreateReq req = new BoardCreateReq("Title", "Content");
        Board board = createBoard(1L, req.title(), req.content(), user);

        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // When
        BoardCreateRes response = boardService.createBoard(req, user);

        // Then
        assertEquals("글 작성 완료", response.msg());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void getBoard_shouldReturnBoardDetails() {
        // Given
        User user = createUser(1L, "user@example.com", UserRoleType.USER);
        Board board = createBoard(1L, "Title", "Content", user);

        // Comment에 User를 설정
        Comment comment = Comment.builder()
                .content("Comment 1")
                .board(board)
                .user(user) // User를 설정
                .build();
        board.setCommentList(Collections.singletonList(comment));

        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);

        // When
        BoardReadRes response = boardService.getBoard(1L);

        // Then
        assertEquals(1L, response.boardId());
        assertEquals("Title", response.title());
        assertEquals("Content", response.content());
        assertEquals(1, response.commentList().size());
        verify(boardRepository, times(1)).findBoardByIdWithThrow(1L);
    }


    @Test
    void updateBoard_shouldUpdateBoardSuccessfully() {
        // Given
        User user = createUser(1L, "user@example.com", UserRoleType.USER);
        Board board = createBoard(1L, "Old Title", "Old Content", user);
        BoardUpdateReq req = new BoardUpdateReq("New Title", "New Content");

        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);

        // When
        BoardUpdateRes response = boardService.updateBoard(1L, user, req);

        // Then
        assertEquals("수정 완료 되었습니다.", response.msg());
        assertEquals("New Title", board.getTitle());
        assertEquals("New Content", board.getContent());
        verify(boardRepository, times(1)).findBoardByIdWithThrow(1L);
    }

    @Test
    void deleteBoard_shouldDeleteBoardSuccessfully() {
        // Given
        User user = createUser(1L, "user@example.com", UserRoleType.USER);
        Board board = createBoard(1L, "Title", "Content", user);

        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);

        // When
        BoardDeleteRes response = boardService.deleteBoard(1L, user);

        // Then
        assertEquals("게시글이 삭제 되었습니다.", response.msg());
        verify(boardRepository, times(1)).delete(board);
        verify(imageFileService, times(1)).deleteImages(1L, "board", user);
    }

    @Test
    void getBestLikeBoard_shouldReturnTop10LikedBoards() {
        // Given
        List<Board> boards = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            boards.add(createBoard((long) i, "Title" + i, "Content" + i, null));
        }
        when(boardRepository.findTop10ByOrderByLikeCountDesc()).thenReturn(boards);

        // When
        List<BestLikeBoardRes> response = boardService.getBestLikeBoard();

        // Then
        assertEquals(10, response.size());
        assertEquals("Title1", response.get(0).title());
        verify(boardRepository, times(1)).findTop10ByOrderByLikeCountDesc();
    }

    @Test
    void searchBoards_shouldReturnPagedResults() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

        // Mock 데이터 생성
        List<BoardsReadRes> boards = Collections.singletonList(
                new BoardsReadRes(1L, "Title", 0L) // BoardsReadRes DTO로 데이터 준비
        );
        Page<BoardsReadRes> boardPage = new PageImpl<>(boards, pageable, 1);

        // Mock 동작 설정
        when(boardRepository.searchBoards(pageable, "keyword")).thenReturn(boardPage);

        // When
        Page<BoardsReadRes> response = boardService.searchBoards(0, 10, "title", true, "keyword");

        // Then
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Title", response.getContent().get(0).title()); // 제목 확인
        assertEquals(0, response.getContent().get(0).boardLikeCount());  // 좋아요 수 확인

        // Repository 호출 여부 검증
        verify(boardRepository, times(1)).searchBoards(pageable, "keyword");
    }
}
