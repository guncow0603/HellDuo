package com.hellduo.domain.comment.service;

import com.hellduo.domain.comment.dto.request.CommentCreatReq;
import com.hellduo.domain.comment.dto.request.CommentUpdateReq;
import com.hellduo.domain.comment.dto.response.CommentCreateRes;
import com.hellduo.domain.comment.dto.response.CommentDeleteRes;
import com.hellduo.domain.comment.dto.response.CommentUpdateRes;
import com.hellduo.domain.comment.entity.Comment;
import com.hellduo.domain.comment.exception.CommentException;
import com.hellduo.domain.comment.repository.CommentRepository;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Board board;
    private Comment comment;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .role(UserRoleType.USER)
                .userStatus(UserStatus.ACTION)
                .gender(Gender.MAN)
                .age(30)
                .nickname("Johnny")
                .weight(70.0)
                .height(175.0)
                .password("password") // 실제 테스트에서 사용되지 않음
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);  // id 설정

        board = Board.builder()
                .title("Board Title")
                .content("Board Content")
                .user(user)
                .build();
        ReflectionTestUtils.setField(board, "id", 1L);  // id 설정

        comment = Comment.builder()
                .content("This is a comment.")
                .user(user)
                .board(board)
                .build();
        ReflectionTestUtils.setField(comment, "id", 1L);  // id 설정
    }

    @Test
    public void testCommentCreate() {
        CommentCreatReq req = new CommentCreatReq("This is a new comment.", 1L);

        when(boardRepository.findBoardByIdWithThrow(1L)).thenReturn(board);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentCreateRes response = commentService.commentCreate(req, user);

        assertEquals("댓글 작성이 완료 되었습니다.", response.msg());
        verify(boardRepository, times(1)).findBoardByIdWithThrow(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testCommentCreateFailEmptyContent() {
        CommentCreatReq req = new CommentCreatReq("", 1L);

        assertThrows(CommentException.class, () -> {
            commentService.commentCreate(req, user);
        });
    }

    @Test
    public void testCommentUpdate() {
        CommentUpdateReq req = new CommentUpdateReq("Updated comment content");

        when(commentRepository.findCommentByIdWithThrow(1L)).thenReturn(comment);

        CommentUpdateRes response = commentService.commentUpdate(req, user, 1L);

        assertEquals("수정 완료.", response.msg());
        assertEquals("Updated comment content", comment.getContent());
        verify(commentRepository, times(1)).findCommentByIdWithThrow(1L);
    }


    @Test
    public void testCommentDelete() {
        when(commentRepository.findCommentByIdWithThrow(1L)).thenReturn(comment);

        CommentDeleteRes response = commentService.commentDelete(user, 1L);

        assertEquals("삭제 완료.", response.msg());
        verify(commentRepository, times(1)).deleteById(1L);
    }

}
