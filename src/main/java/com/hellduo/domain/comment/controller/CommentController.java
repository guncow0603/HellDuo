package com.hellduo.domain.comment.controller;

import com.hellduo.domain.comment.dto.request.CommentCreatReq;
import com.hellduo.domain.comment.dto.request.CommentReadReq;
import com.hellduo.domain.comment.dto.request.CommentUpdateReq;
import com.hellduo.domain.comment.dto.response.CommentCreateRes;
import com.hellduo.domain.comment.dto.response.CommentReadRes;
import com.hellduo.domain.comment.dto.response.CommentUpdateRes;
import com.hellduo.domain.comment.service.CommentService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateRes> commentCreate(
                @RequestBody CommentCreatReq req,
                @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.commentCreate(req,userDetails.getUser().getId()));
    }

    @GetMapping
    public ResponseEntity<List<CommentReadRes>> commentRead(
            @RequestBody CommentReadReq req
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.commentRead(req));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateRes>commentUpdate(
            @RequestBody CommentUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.commentUpdate(
                req, userDetails.getUser(), commentId));
    }
}
