package com.hellduo.domain.imageFile.controller;

import com.hellduo.domain.imageFile.dto.response.*;
import com.hellduo.domain.imageFile.entitiy.BannerImage;
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

    // PT 이미지 업로드
    @PostMapping("/pt/{ptId}")
    public ResponseEntity<UserImageCreateRes> ptUploadImages(
            @PathVariable Long ptId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.ptUploadImages(ptId,userDetails.getUser(), multipartFiles));
    }

    // PT 이미지 조회
    @GetMapping("/pt/{ptId}")
    public ResponseEntity<List<PTImageReadRes>> readPTImages(@PathVariable Long ptId) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readPTImages(ptId));
    }

    // PT 썸네일 이미지 조회
    @GetMapping("/pt/thumbnail/{ptId}")
    public ResponseEntity<PTImageReadRes> readThumbnailPTImage(@PathVariable Long ptId) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readThumbnailPTImage(ptId));
    }

    // 프로필 이미지 조회
    @GetMapping("/profile")
    public ResponseEntity<UserImageReadRes> readUserProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readUserProfileImage(userDetails.getUser().getId()));
    }

    // 프로필 이미지 조회
    @GetMapping("/profile/{trainerId}")
    public ResponseEntity<UserImageReadRes> getUserProfileImage(
            @PathVariable Long trainerId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.getUserProfileImage(trainerId));
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

    // 배너 이미지 업로드
    @PostMapping("/banner")
    public ResponseEntity<BannerImageCreateRes> bannerUploadImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "banners", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.bannerUploadImages(userDetails.getUser(), multipartFiles));
    }

    // 배너 이미지 조회
    @GetMapping("/banner")
    public ResponseEntity<List<BannerReadRes>> readBannerImages(
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.readBannerImages());
    }

    // 배너 이미지 조회
    @DeleteMapping("/banner/{bannerId}")
    public ResponseEntity<BannerImageDeleteRes> readBannerImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long bannerId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(imageFileService.deleteBannerImages(userDetails.getUser(),bannerId));
    }


}
