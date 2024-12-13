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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 게시글 작성 (트랜잭션 적용)
    @Transactional
    public BoardCreateRes createBoard(BoardCreateReq req, User user) {
        Board board = Board.builder()
                .title(req.title())
                .content(req.content())
                .user(user)
                .build();
        boardRepository.save(board); // DB에 저장
        return new BoardCreateRes("글 작성 완료");
    }

    // 게시글 조회 (읽기 전용 트랜잭션 적용)
    @Transactional(readOnly = true)
    public BoardReadRes getBoardById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId); // 조회만 하는 메서드이므로 읽기 전용 트랜잭션 적용
        return new BoardReadRes(board.getId(), board.getLikeCount(), board.getTitle(), board.getContent());
    }

    // 모든 게시글 조회 (읽기 전용 트랜잭션 적용)
    @Transactional(readOnly = true)
    public List<BoardsReadRes> getBoards() {
        List<Board> boards = boardRepository.findAll(); // 모든 게시글을 조회만 하므로 읽기 전용 트랜잭션 적용
        List<BoardsReadRes> boardsReadResList = new ArrayList<>();
        for (Board board : boards) {
            boardsReadResList.add(new BoardsReadRes(board.getId(), board.getTitle(), board.getLikeCount()));
        }
        return boardsReadResList;
    }

    // 게시글 수정 (트랜잭션 적용)
    @Transactional
    public BoardUpdateRes updateBoard(Long boardId, User user, BoardUpdateReq req) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId); // 게시글 조회
        if (!board.getUser().getId().equals(user.getId())) { // 사용자 확인
            throw new BoardException(BoardErrorCode.BOARD_CURRENT_USER);
        }

        if (req.title() != null) {
            board.updateTitle(req.title()); // 제목 업데이트
        }
        if (req.content() != null) {
            board.updateContent(req.content()); // 내용 업데이트
        }
        return new BoardUpdateRes("수정 완료 되었습니다.");
    }

    // 게시글 삭제 (트랜잭션 적용)
    @Transactional
    public BoardDeleteRes deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findBoardByIdWithThrow(boardId); // 게시글 조회
        if (!board.getUser().getId().equals(user.getId())) { // 사용자 확인
            throw new BoardException(BoardErrorCode.BOARD_CURRENT_USER);
        }
        boardRepository.delete(board); // 게시글 삭제
        return new BoardDeleteRes("게시글이 삭제 되었습니다.");
    }

    // 좋아요가 많은 게시글 조회 (읽기 전용 트랜잭션 적용)
    @Transactional(readOnly = true)
    public List<BestLikeBoardRes> getBestLikeBoard() {
        List<Board> top10Boards = boardRepository.findTop10ByOrderByLikeCountDesc(); // 좋아요 순으로 상위 10개 게시글 조회
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

    // 게시글 검색 (읽기 전용 트랜잭션 적용)
    @Transactional(readOnly = true)
    public List<BoardsReadRes> searchBoards(String keyword) {
        List<Board> boards = boardRepository.searchByKeyword(keyword); // 검색어로 게시글 검색
        List<BoardsReadRes> boardsReadResList = new ArrayList<>();
        for (Board board : boards) {
            boardsReadResList.add(new BoardsReadRes(board.getId(), board.getTitle(), board.getLikeCount()));
        }
        return boardsReadResList;
    }
}