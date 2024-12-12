package com.hellduo.domain.review.controller;

import com.hellduo.domain.review.dto.request.ReviewCreateReq;
import com.hellduo.domain.review.dto.response.GetReviewsRes;
import com.hellduo.domain.review.dto.response.ReviewCreateRes;
import com.hellduo.domain.review.service.ReviewService;
import com.hellduo.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{ptId}")
    public ResponseEntity<ReviewCreateRes> reviewCreate(
            @PathVariable Long ptId,
            @Valid @RequestBody ReviewCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.reviewCreate(req, userDetails.getUser(),ptId));
    }
    @GetMapping
    public ResponseEntity<List<GetReviewsRes>>getReviews(){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviews());
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<List<GetReviewsRes>>getTrainerReviews(
            @PathVariable Long trainerId){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getTrainerReviews(trainerId));
    }
}

