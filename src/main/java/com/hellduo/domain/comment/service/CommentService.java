package com.hellduo.domain.comment.service;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.comment.dto.request.CommentCreatReq;
import com.hellduo.domain.comment.dto.request.CommentReadReq;
import com.hellduo.domain.comment.dto.request.CommentUpdateReq;
import com.hellduo.domain.comment.dto.response.CommentCreateRes;
import com.hellduo.domain.comment.dto.response.CommentReadRes;
import com.hellduo.domain.comment.dto.response.CommentUpdateRes;
import com.hellduo.domain.comment.entity.Comment;
import com.hellduo.domain.comment.exception.CommentErrorCode;
import com.hellduo.domain.comment.exception.CommentException;
import com.hellduo.domain.comment.repository.CommentRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CommentCreateRes commentCreate(CommentCreatReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);

        Board board = boardRepository.findBoardByIdWithThrow(req.boardId());

        if(req.content() == null) {
            throw new CommentException(CommentErrorCode.NOT_FOUND_COMMENT);
        }

        Comment comment = Comment.builder()
                .user(user)
                .content(req.content())
                .board(board)
                .build();

        commentRepository.save(comment);

        return new CommentCreateRes("댓글 작성이 완료 되었습니다.");
    }

    public List<CommentReadRes> commentRead(CommentReadReq req) {

        Board board = boardRepository.findBoardByIdWithThrow(req.boardId());

        List<Comment> commentList = commentRepository.findAllByBoard(board);

        List<CommentReadRes> commentReadResList = new ArrayList<>();

        for(Comment comment : commentList){
            commentReadResList.add(new CommentReadRes(comment.getContent(),comment.getUser().getNickname()));
        }

        return commentReadResList;
    }

    public CommentUpdateRes commentUpdate(CommentUpdateReq req, User user, Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithThrow(commentId);

        if(user.getId() != comment.getUser().getId()) {
            throw new UserException(UserErrorCode.NOT_FOUND_USER);
        }

        comment.updateContent(req.Content());

        return new CommentUpdateRes("수정 완료.");
    }
}
