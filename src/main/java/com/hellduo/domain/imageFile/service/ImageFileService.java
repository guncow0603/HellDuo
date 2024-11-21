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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageFileService {

    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    // 프로필 이미지 업로드
    public UserImageCreateRes uploadUserProfileImage(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findUserByIdWithThrow(userId);
        String fileUrl = uploadFileToS3(multipartFile, userId, "users/profiles/");

        UserImage userImage = UserImage.builder()
                .user(user)
                .userImageUrl(fileUrl)
                .type(ImageType.PROFILE_IMG)
                .build();

        userImageRepository.save(userImage);
        return new UserImageCreateRes("이미지 등록 완료");
    }

    // 자격증 이미지 업로드
    public UserImageCreateRes uploadUserCertificationImages(Long userId, List<MultipartFile> multipartFiles) {
        User user = userRepository.findUserByIdWithThrow(userId);
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "users/certifications/");

        List<UserImage> userImageList = createUserImageList(fileUrlList, user);
        userImageRepository.saveAll(userImageList);
        return new UserImageCreateRes("이미지 등록 완료");
    }

    // 단일 파일 S3 업로드
    private String uploadFileToS3(MultipartFile multipartFile, Long userId, String filePath) {
        // 업로드 경로 설정
        String userImageUrl = filePath + userId;

        // 파일을 업로드하고 URL 반환
        return s3Uploader.uploadSingleFileToS3(multipartFile, userImageUrl);
    }

    // 다수 파일 S3 업로드
    private List<String> uploadFilesToS3(List<MultipartFile> multipartFiles, Long userId, String filePath) {
        List<String> fileUrls = new ArrayList<>();

        // 업로드 경로 설정
        String userImageUrl = filePath + userId;

        // 파일을 업로드하고 URL 목록 반환
        List<String> uploadedFileUrls = s3Uploader.uploadFileToS3(multipartFiles, userImageUrl);
        fileUrls.addAll(uploadedFileUrls);

        return fileUrls;
    }

    // UserImage 객체 리스트 생성
    private List<UserImage> createUserImageList(List<String> fileUrls, User user) {
        List<UserImage> userImageList = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            UserImage userImage = UserImage.builder()
                    .user(user)
                    .userImageUrl(fileUrl)
                    .type(ImageType.CERTS_IMG)
                    .build();
            userImageList.add(userImage);
        }
        return userImageList;
    }
}