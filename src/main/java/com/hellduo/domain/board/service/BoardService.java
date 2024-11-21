package com.hellduo.domain.board.service;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.response.BoardCreateRes;
import com.hellduo.domain.board.dto.response.BoardReadRes;
import com.hellduo.domain.board.dto.response.BoardsReadRes;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<BoardsReadRes> getBoards() {
        List<Board> boards = boardRepository.findAll(); // 모든 게시글 조회

        // Board 객체에서 제목을 추출하여 BoardsReadRes 객체 생성
        List<BoardsReadRes> boardsReadResList = new ArrayList<>();
        for (Board board : boards) {
            boardsReadResList.add(new BoardsReadRes(board.getTitle())); // 제목만 BoardsReadRes에 추가
        }

        return boardsReadResList; // BoardsReadRes 리스트 반환
    }
}
