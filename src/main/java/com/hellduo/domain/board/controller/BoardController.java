package com.hellduo.domain.board.controller;

import com.hellduo.domain.board.dto.response.BoardReadRes;
import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.response.BoardCreateRes;
import com.hellduo.domain.board.service.BoardService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController{
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardCreateRes>creteBoard(@Valid @RequestBody BoardCreateReq req,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(boardService.createBoard(req,userDetails.getUser().getId()));
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardReadRes> getBoardById(@PathVariable Long id) {
        BoardReadRes board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

}
