package com.hellduo.domain.board.service;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.response.BoardCreateRes;
import com.hellduo.domain.board.dto.response.BoardReadRes;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.Buffer;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardCreateRes createBoard(BoardCreateReq req, Long id) {
        User user = userRepository.findUserByIdWithThrow(id);

        Board board = Board.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();
        boardRepository.save(board);
        return new BoardCreateRes("글 작성 완료");
    }

    public BoardReadRes getBoardById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId);
        return new BoardReadRes(board.getTitle(), board.getContent());
    }
}
