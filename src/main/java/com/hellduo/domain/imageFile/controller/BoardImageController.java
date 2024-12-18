package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.*;
import com.hellduo.domain.imageFile.service.BoardImageService;
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
@RequestMapping("/api/v1/boards/{boardId}/images")
public class BoardImageController {
    private final BoardImageService boardImageService;
    @PostMapping
    public ResponseEntity<UploadBoardImagesRes> uploadBoardImages(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardImageService.uploadBoardImages(userDetails.getUser(), multipartFiles, boardId));
    }

    // 게시판 이미지 조회
    @GetMapping
    public ResponseEntity<List<GetBoardImagesRes>> getBoardImages(
            @PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardImageService.getBoardImages(boardId));
    }

    // 게시판 썸네일 이미지 조회
    @GetMapping("/thumbnail")
    public ResponseEntity<GetBoardImageRes> getBoardImage(
            @PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardImageService.getBoardImage(boardId));
    }
}
