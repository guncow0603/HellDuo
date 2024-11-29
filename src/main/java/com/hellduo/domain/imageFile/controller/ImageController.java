package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.UserCertsReadRes;
import com.hellduo.domain.imageFile.dto.response.UserImageCreateRes;
import com.hellduo.domain.imageFile.dto.response.UserImageDeleteRes;
import com.hellduo.domain.imageFile.dto.response.UserImageReadRes;
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
@RequestMapping("/api/v1/userImage")
public class ImageController {

    private final ImageFileService imageFileService;

    // 프로필 이미지 수정
    @PostMapping("/profile")
    public ResponseEntity<UserImageCreateRes> updateUserProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageFileService.updateUserProfileImage(userDetails.getUser().getId(), multipartFile));
    }

    // 자격증 이미지 업로드
    @PostMapping("/certifications")
    public ResponseEntity<UserImageCreateRes> uploadUserCertificationImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.uploadUserCertificationImages(userDetails.getUser().getId(), multipartFiles));
    }

    // 프로필 이미지 조회
    @GetMapping("/profile")
    public ResponseEntity<UserImageReadRes> readUserProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readUserProfileImage(userDetails.getUser().getId()));
    }

    // 자격증 이미지 조회
    @GetMapping("/certifications/{trainerId}")
    public ResponseEntity<List<UserCertsReadRes>> readUserCertImages(@PathVariable Long trainerId) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readUserCertImages(trainerId));
    }

    // 자격증 이미지 삭제
    @DeleteMapping("/certifications/{certId}")
    public ResponseEntity<UserImageDeleteRes> deleteUserCertificationImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long certId) {

        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.deleteUserCertificationImage(userDetails.getUser().getId(), certId));
    }



}
