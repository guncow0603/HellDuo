package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.UserImageCreateRes;
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
@RequestMapping("/api/v1")
public class ImageController {

    private final ImageFileService imageFileService;

    // 프로필 이미지 업로드
    @PostMapping("/userImage/profile")
    public ResponseEntity<UserImageCreateRes> uploadUserProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.uploadUserProfileImage(userDetails.getUser().getId(), multipartFile));
    }

    // 자격증 이미지 업로드
    @PostMapping("/userImage/certifications")
    public ResponseEntity<UserImageCreateRes> uploadUserCertificationImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.uploadUserCertificationImages(userDetails.getUser().getId(), multipartFiles));
    }
}
