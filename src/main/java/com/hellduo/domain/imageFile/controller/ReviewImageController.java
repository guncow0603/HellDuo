package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.GetBoardImagesRes;
import com.hellduo.domain.imageFile.dto.response.GetReviewImagesRes;
import com.hellduo.domain.imageFile.dto.response.UploadBoardImagesRes;
import com.hellduo.domain.imageFile.dto.response.UploadReviewImagesRes;
import com.hellduo.domain.imageFile.service.BoardImageService;
import com.hellduo.domain.imageFile.service.ReviewImageService;
import com.hellduo.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews/{reviewId}/images")
public class ReviewImageController {
    private final ReviewImageService reviewImageService;
    @PostMapping
    public ResponseEntity<UploadReviewImagesRes> uploadReviewImages(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewImageService.uploadReviewImages(userDetails.getUser(), multipartFiles, reviewId));
    }

    // 게시판 이미지 조회
    @GetMapping
    public ResponseEntity<List<GetReviewImagesRes>> getReviewImages(
            @PathVariable Long reviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewImageService.getReviewImages(reviewId));
    }
}

