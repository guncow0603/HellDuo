package com.hellduo.domain.board_like.controller;

import com.hellduo.domain.board_like.dto.response.LikeResponse;
import com.hellduo.domain.board_like.service.BoardLikeService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boardLike")
public class  BoardLikeController {
    private final BoardLikeService boardLikeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<LikeResponse>boardLikeToggle(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardLikeService.boardLikeToggle(boardId, userDetails.getUser()));
    }
}
