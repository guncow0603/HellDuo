package com.hellduo.domain.board.controller;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.request.BoardUpdateReq;
import com.hellduo.domain.board.dto.response.*;
import com.hellduo.domain.board.service.BoardService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/board")
public class BoardController{
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardCreateRes>creteBoard(@Valid @RequestBody BoardCreateReq req,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(boardService.createBoard(req,userDetails.getUser()));
    }

    // 특정 게시글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardReadRes>getBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoard(boardId));
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<BoardsReadRes>> getBoards() {
        return ResponseEntity.status(HttpStatus.OK).body( boardService.getBoards());
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardUpdateRes>updateBoard(@RequestBody BoardUpdateReq req,
                                                     @PathVariable Long boardId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                boardService.updateBoard(boardId, userDetails.getUser(),req));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<BoardDeleteRes>deleteBoard(@PathVariable Long boardId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                boardService.deleteBoard(boardId, userDetails.getUser()));
    }

    @GetMapping("/bestLike")
    public ResponseEntity<List<BestLikeBoardRes>>getBestLikeBoard() {
        return ResponseEntity.status(HttpStatus.OK).body(
                boardService.getBestLikeBoard()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<PageBoardsRes> searchBoards(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String keyword
    ) {
        Page<BoardsReadRes> pageResult = boardService.searchBoards(page - 1, size, sortBy, isAsc, keyword);
        List<BoardsReadRes> boards = pageResult.getContent();  // 실제 데이터 추출

        // PagedBoardsRes 객체 생성하여 반환
        PageBoardsRes pageBoardsRes = new PageBoardsRes(
                boards,
                pageResult.getTotalPages(),  // 전체 페이지 수
                pageResult.getNumber()    // 현재 페이지 번호 (1부터 시작하도록 +1)
        );

        return ResponseEntity.status(HttpStatus.OK).body(pageBoardsRes);
    }

}
