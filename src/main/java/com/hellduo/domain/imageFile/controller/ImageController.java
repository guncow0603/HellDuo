package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.GetImagesRes;
import com.hellduo.domain.imageFile.dto.response.ImageDeleteRes;
import com.hellduo.domain.imageFile.dto.response.UploadImagesRes;
import com.hellduo.domain.imageFile.dto.response.GetThumbnailRes;
import com.hellduo.domain.imageFile.service.ImageFileService;
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
@RequestMapping("/api/v2/images")
public class ImageController {

    private final ImageFileService imageFileService;

    // 공통 이미지 업로드
    @PostMapping("/{category}/{targetId}")
    public ResponseEntity<UploadImagesRes> uploadImages(
            @PathVariable String category, // profile, certifications, pt, board, review
            @PathVariable Long targetId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageFileService.uploadImages(category, targetId, userDetails.getUser(), multipartFiles));
    }

    // 공통 이미지 조회
    @GetMapping("/{category}/{targetId}")
    public ResponseEntity<List<GetImagesRes>> getImages(
            @PathVariable String category,
            @PathVariable Long targetId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageFileService.getImages(category, targetId));
    }

    // 썸네일 이미지 조회
    @GetMapping("/{category}/{targetId}/thumbnail")
    public ResponseEntity<GetThumbnailRes> getThumbnailImage(
            @PathVariable String category,
            @PathVariable Long targetId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageFileService.getThumbnailImage(category, targetId));
    }

    // 단일 이미지 삭제
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ImageDeleteRes> deleteImage(
            @PathVariable Long imageId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageFileService.deleteImage( imageId, userDetails.getUser()));
    }
}
