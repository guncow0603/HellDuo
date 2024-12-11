package com.hellduo.domain.review.controller;

import com.hellduo.domain.review.dto.request.ReviewCreateReq;
import com.hellduo.domain.review.dto.response.ReviewCreateRes;
import com.hellduo.domain.review.service.ReviewService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewCreateRes> reviewCreate(
            @Valid @RequestBody ReviewCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.reviewCreate(req, userDetails.getUser()));
    }
}

