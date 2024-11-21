package com.hellduo.domain.imageFile.service;

import com.hellduo.domain.imageFile.dto.response.UserImageCreateRes;
import com.hellduo.domain.imageFile.entitiy.ImageType;
import com.hellduo.domain.imageFile.entitiy.UserImage;
import com.hellduo.domain.imageFile.repository.UserImageRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageFileService {

    private final S3Uploader s3Uploader;

    private final UserRepository userRepository;

    private final UserImageRepository userImageRepository;

    public UserImageCreateRes userImageCreate(Long userId, MultipartFile multipartFiles) {
        User user = userRepository.findUserByIdWithThrow(userId);
        String fileUrl = s3Upload(multipartFiles, userId,"users/profiles/");

        UserImage userImage = UserImage.builder()
                                       .user(user)
                                       .userImageUrl(fileUrl)
                                       .type(ImageType.PROFILE_IMG)
                                       .build();

        userImageRepository.save(userImage);
        return new UserImageCreateRes("이미지 등록 완료");
    }

    public String s3Upload(MultipartFile multipartFile, Long userId, String filePath) {
        // 업로드 경로 설정
        String userImageUrl = filePath + userId;

        // 파일을 업로드하고 URL 반환
        return s3Uploader.uploadSingleFileToS3(multipartFile, userImageUrl);
    }
}
