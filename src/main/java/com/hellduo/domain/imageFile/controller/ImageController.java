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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ImageController {

    private final ImageFileService imageFileService;

    @PostMapping("/userImage")
    public ResponseEntity<UserImageCreateRes> userImageCreate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "file", required = false) MultipartFile multipartFiles){
        return ResponseEntity.status(HttpStatus.CREATED).body(imageFileService.userImageCreate(userDetails.getUser().getId(),multipartFiles));
    }
}
