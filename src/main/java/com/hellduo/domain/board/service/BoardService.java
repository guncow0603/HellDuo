package com.hellduo.domain.board.service;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.request.BoardUpdateReq;
import com.hellduo.domain.board.dto.response.*;
import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.exception.BoardErrorCode;
import com.hellduo.domain.board.exception.BoardException;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardCreateRes createBoard(BoardCreateReq req, User user) {
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
        return new BoardReadRes(board.getId(), board.getLikeCount(), board.getTitle(), board.getContent());
    }

    public List<BoardsReadRes> getBoards() {
        List<Board> boards = boardRepository.findAll(); // 모든 게시글 조회

        // Board 객체에서 제목을 추출하여 BoardsReadRes 객체 생성
        List<BoardsReadRes> boardsReadResList = new ArrayList<>();
        for (Board board : boards) {
            boardsReadResList.add(new BoardsReadRes(
                    board.getId(),
                    board.getTitle(),
                    board.getLikeCount()));
        }
        return boardsReadResList; // BoardsReadRes 리스트 반환
    }

    public BoardUpdateRes updateBoard(Long boardId, User user, BoardUpdateReq req) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId);
        if(!board.getUser().getId() .equals(user.getId()) ) {
            throw new BoardException(BoardErrorCode.BOARD_CURRENT_USER);
        }
        if(req.title() != null) {board.updateTitle(req.title());}
        if(req.content() != null) {board.updateContent(req.content());}
        return new BoardUpdateRes("수정 완료 되었습니다.");
    }

    public BoardDeleteRes deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId);
        if(!board.getUser().getId() .equals(user.getId()) ) {
            throw new BoardException(BoardErrorCode.BOARD_CURRENT_USER);
        }
        boardRepository.delete(board);
        return new BoardDeleteRes("게시글이 삭제 되었습니다.");
    }

    public List<BestLikeBoardRes> getBestLikeBoard() {
        // 데이터베이스에서 좋아요 순으로 상위 10개 게시글 가져오기
        List<Board> top10Boards = boardRepository.findTop10ByOrderByLikeCountDesc();
        // DTO 변환
        List<BestLikeBoardRes> result = new ArrayList<>();
        for (Board board : top10Boards) {
            BestLikeBoardRes dto = new BestLikeBoardRes(
                    board.getId(),
                    board.getLikeCount(),
                    board.getTitle(),
                    board.getContent()); // 엔티티를 DTO로 변환
            result.add(dto);
        }
        return result;
    }

    public List<BoardsReadRes> searchBoards(String keyword) {
        List<Board> boards = boardRepository.searchByKeyword(keyword);

        List<BoardsReadRes> boardsReadResList = new ArrayList<>();

        for (Board board : boards) {
            boardsReadResList.add(new BoardsReadRes(
                    board.getId(),
                    board.getTitle(),
                    board.getLikeCount()));
        }

        return boardsReadResList;
    }
}
