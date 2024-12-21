package com.hellduo.domain.board.service;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.request.BoardUpdateReq;
import com.hellduo.domain.board.dto.response.*;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private User user;

    @Mock
    private Board board;
    @BeforeEach
    public void setUp() {
        // User 객체 생성
        user = User.builder()
                .email("test@example.com")
                .password("password123")
                .role(UserRoleType.USER)  // 적절한 role 설정
                .nickname("testUser")
                .gender(Gender.MAN)  // 적절한 gender 설정
                .age(30)
                .weight(70.0)
                .height(175.0)
                .phoneNumber("010-1234-5678")
                .name("Test User")
                .specialization(Specialization.CROSSFIT)  // 적절한 specialization 설정
                .experience(5)
                .certifications("Certification A")
                .bio("Test Bio")
                .userStatus(UserStatus.ACTION)  // 적절한 상태 설정
                .build();
        // 테스트용 게시글 객체 설정
        board = Board.builder()
                .title("Test Title")
                .content( "Test Content")
                .user(user)
                .build();
    }




    @Test
    public void testCreateBoard() {
        board = new Board("Test Title", "Test Content", user);
        ReflectionTestUtils.setField(board, "id", 1L);  // Board 객체의 id 설정
        // Given
        BoardCreateReq req = new BoardCreateReq("Test Title", "Test Content");
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // When
        BoardCreateRes response = boardService.createBoard(req, user);

        response= new BoardCreateRes(board.getId(), "글 작성 완료");
        // Then
        assertNotNull(response);
        assertEquals("글 작성 완료", response.msg());
        assertEquals(1L, response.boardId());
    }

    @Test
    public void testGetBoard() {
        // Given
        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);
        ReflectionTestUtils.setField(board, "id", 1L);  // Board 객체의 id 설정

        // When
        BoardReadRes response = boardService.getBoard(1L);

        // Then
        assertNotNull(response);
        assertEquals(board.getId(), response.boardId());
        assertEquals(board.getTitle(), response.title());
        assertEquals(board.getContent(), response.content());
    }

    @Test
    public void testUpdateBoard() {
        // Given
        BoardUpdateReq req = new BoardUpdateReq("Updated Title", "Updated Content");
        ReflectionTestUtils.setField(board, "id", 1L);  // Board 객체의 id 설정
        ReflectionTestUtils.setField(user, "id", 1L);   // User 객체의 id 설정
        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);

        // When
        BoardUpdateRes response = boardService.updateBoard(1L, user, req);

        // Then
        assertEquals("수정 완료 되었습니다.", response.msg());
        assertEquals("Updated Title", board.getTitle());
        assertEquals("Updated Content", board.getContent());
    }


    @Test
    public void testGetBestLikeBoard() {
        // Given
        Board topBoard = new Board("Best Title", "Best Content", user);
        ReflectionTestUtils.setField(topBoard, "id", 2L);  // Board 객체의 id 설정
        ReflectionTestUtils.setField(topBoard, "likeCount", 100L);  // Board 객체의 likeCount 설정
        when(boardRepository.findTop10ByOrderByLikeCountDesc()).thenReturn(List.of(topBoard));

        // When
        List<BestLikeBoardRes> response = boardService.getBestLikeBoard();

        // Then
        assertEquals(1, response.size());
        assertEquals(topBoard.getId(), response.get(0).boardId());
        assertEquals(topBoard.getLikeCount(), response.get(0).boardLikeCount());
        assertEquals(topBoard.getTitle(), response.get(0).title());
    }

    @Test
    public void testSearchBoards() {
        // Given
        String keyword = "Test";
        Board board1 = new Board("Test Title", "Test Content", user);
        ReflectionTestUtils.setField(board1, "id", 1L);  // Board 객체의 id 설정
        when(boardRepository.searchByKeyword(keyword)).thenReturn(List.of(board1));

        // When
        List<BoardsReadRes> response = boardService.searchBoards(keyword);

        // Then
        assertEquals(1, response.size());
        assertEquals(board1.getId(), response.get(0).boardId());
    }


}