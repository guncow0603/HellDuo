package com.hellduo.domain.board.controller;

import com.hellduo.domain.board.dto.request.BoardCreateReq;
import com.hellduo.domain.board.dto.response.BoardCreateRes;
import com.hellduo.domain.board.service.BoardService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
