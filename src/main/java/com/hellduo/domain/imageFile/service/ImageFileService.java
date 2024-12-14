package com.hellduo.domain.imageFile.service;

import com.hellduo.domain.imageFile.dto.response.*;
import com.hellduo.domain.imageFile.entity.BannerImage;
import com.hellduo.domain.imageFile.entity.PTImage;
import com.hellduo.domain.imageFile.entity.enums.ImageType;
import com.hellduo.domain.imageFile.entity.UserImage;
import com.hellduo.domain.imageFile.exception.ImageErrorCode;
import com.hellduo.domain.imageFile.exception.ImageException;
import com.hellduo.domain.imageFile.repository.BannerRepository;
import com.hellduo.domain.imageFile.repository.PTImageRepository;
import com.hellduo.domain.imageFile.repository.UserImageRepository;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final PTImageRepository ptImageRepository;
    private final BannerRepository bannerRepository;
    private final PTRepository ptRepository;

    @Value("${s3.url}")
    private String s3Url;

    public UserImageCreateRes updateUserProfileImage(Long userId, MultipartFile multipartFile) {
        // 기존 프로필 이미지 조회 (있으면 삭제)
        UserImage userImage = userImageRepository.findProfileByUserIdAndType(userId, ImageType.PROFILE_IMG)
                .orElse(null);

        // 기존 이미지가 있으면 S3에서 삭제하고 DB에서 삭제
        if (userImage != null) {
            String imageUrl = userImage.getUserImageUrl();
            String s3Key = imageUrl.replace(s3Url, ""); // s3Url을 정확히 지정했는지 확인
            s3Uploader.deleteS3(s3Key); // 기존 이미지 S3에서 삭제
            userImageRepository.delete(userImage); // DB에서 삭제
        }

        // 새로운 이미지 파일을 S3에 업로드
        String fileUrl = uploadFileToS3(multipartFile, userId, "users/profiles/");

        User user = userRepository.findUserByIdWithThrow(userId);
        // 새로운 프로필 이미지 저장
        UserImage newUserImage = UserImage.builder()
                .user(user)
                .userImageUrl(s3Url + fileUrl) // s3Url과 fileUrl을 결합하여 완전한 URL 생성
                .type(ImageType.PROFILE_IMG)   // 이미지 타입 설정
                .build();

        userImageRepository.save(newUserImage); // DB에 새로운 이미지 저장

        // 수정 완료 응답
        return new UserImageCreateRes("이미지 수정 완료");
    }

    // 자격증 이미지 업로드
    public UserImageCreateRes uploadUserCertificationImages(Long userId, List<MultipartFile> multipartFiles) {
        User user = userRepository.findUserByIdWithThrow(userId);
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "users/certifications/");

        List<UserImage> userImageList = createUserImageList(fileUrlList, user);
        userImageRepository.saveAll(userImageList);
        return new UserImageCreateRes("자격증 이미지가 업로드되었습니다.");
    }

    // 프로필 이미지 조회
    public UserImageReadRes readUserProfileImage(Long userId) {
        UserImage userImage = userImageRepository.findProfileByUserIdAndType(userId, ImageType.PROFILE_IMG)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_PROFILE));
        return new UserImageReadRes(userImage.getId(),userImage.getUserImageUrl());
    }

    // 자격증 이미지 조회
    public List<UserCertsReadRes> readUserCertImages(Long trainerId) {
        // 자격증 이미지들을 조회
        List<UserImage> userImages = userImageRepository.findCertificationsByUserIdAndType(trainerId, ImageType.CERTS_IMG);

        if (userImages.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE); // 자격증 이미지가 없을 경우 예외 처리
        }

        // 결과를 담을 리스트
        List<UserCertsReadRes> response = new ArrayList<>();

        // UserImage 객체들을 UserCertsReadRes로 변환하여 리스트에 추가
        for (UserImage userImage : userImages) {
            response.add(new UserCertsReadRes(userImage.getId(),userImage.getUserImageUrl()));
        }

        // 변환된 리스트 반환
        return response;
    }



    // 단일 자격증 이미지 삭제
    public UserImageDeleteRes deleteUserCertificationImage(Long userId, Long imageId) {
        // 사용자 확인
        User user = userRepository.findUserByIdWithThrow(userId);

        // 이미지 조회
        UserImage userImage = userImageRepository.findByUserAndId(user, imageId)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_IMAGE));

        // S3에서 이미지 삭제
        String imageUrl = userImage.getUserImageUrl();
        String s3Key = imageUrl.replace(s3Url, "");
        s3Uploader.deleteS3(s3Key);

        // DB에서 이미지 삭제
        userImageRepository.delete(userImage);

        return new UserImageDeleteRes("삭제 완료");
    }








    // UserImage 객체 리스트 생성
    private List<UserImage> createUserImageList(List<String> fileUrls, User user) {
        List<UserImage> userImageList = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            UserImage userImage = UserImage.builder()
                    .user(user)
                    .userImageUrl(s3Url+fileUrl)
                    .type(ImageType.CERTS_IMG)
                    .build();
            userImageList.add(userImage);
        }
        return userImageList;
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

    private List<PTImage> createImageList(PT pt,List<String> fileUrls, User user) {
        List<PTImage> ptImageList = new ArrayList<>();

        // 첫 번째 이미지를 썸네일로 설정, 나머지는 일반 이미지로 설정
        for (int i = 0; i < fileUrls.size(); i++) {
            String fileUrl = fileUrls.get(i);

            // 첫 번째 이미지는 썸네일로, 그 외의 이미지는 일반 사진으로 설정
            ImageType imageType = (i == 0) ? ImageType.THUMBNAIL : ImageType.REGULAR;

            PTImage ptImage = PTImage.builder()
                    .user(user)
                    .pt(pt)
                    .userImageUrl(s3Url + fileUrl)
                    .type(imageType)  // 이미지 타입 설정
                    .build();

            ptImageList.add(ptImage);
        }
        return ptImageList;
    }
    public UserImageCreateRes ptUploadImages(Long ptId,User user, List<MultipartFile> multipartFiles) {
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, ptId, "ptImages/");
        PT pt = ptRepository.findPTByIdWithThrow(ptId);
        List<PTImage> ptImageList = createImageList(pt,fileUrlList, user);
        ptImageRepository.saveAll(ptImageList);
        return new UserImageCreateRes("이미지가 업로드되었습니다.");
    }

    public List<PTImageReadRes> readPTImages(Long ptId) {

        List<PTImage> ptImages = ptImageRepository.findAllByPtId(ptId);

        if (ptImages.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }

        // 결과를 담을 리스트
        List<PTImageReadRes> response = new ArrayList<>();

        // PTImage 객체들을 UserCertsReadRes로 변환하여 리스트에 추가
        for (PTImage ptImage : ptImages) {
            response.add(new PTImageReadRes(ptImage.getId(),ptImage.getUserImageUrl()));
        }

        // 변환된 리스트 반환
        return response;
    }

    public BannerImageCreateRes bannerUploadImages(User user, List<MultipartFile> multipartFiles) {
        if(user.getRole()!= UserRoleType.ADMIN){
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, user.getId(), "banners/");

        List<BannerImage> bannerImageList = createBannerImageList(fileUrlList, user);
        bannerRepository.saveAll(bannerImageList);
        return new BannerImageCreateRes("이미지가 업로드되었습니다.");
    }

    private List<BannerImage> createBannerImageList(List<String> fileUrls, User user) {
        List<BannerImage> bannerImageList = new ArrayList<>();

        // 첫 번째 이미지를 썸네일로 설정, 나머지는 일반 이미지로 설정
        for (String fileUrl : fileUrls) {
            BannerImage bannerImage = BannerImage.builder()
                    .user(user)
                    .userImageUrl(s3Url + fileUrl)
                    .build();

            bannerImageList.add(bannerImage);
        }
        return bannerImageList;
    }

    public List<BannerReadRes> readBannerImages() {
        List<BannerImage> bannerImageList = bannerRepository.findAll();

        if (bannerImageList.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }
        List<BannerReadRes> response = new ArrayList<>();

        for (BannerImage bannerImage : bannerImageList) {
            response.add(new BannerReadRes(bannerImage.getId(),bannerImage.getUserImageUrl()));
        }

        // 변환된 리스트 반환
        return response;
    }

    public BannerImageDeleteRes deleteBannerImages(User user, Long bannerId) {
        if(user.getRole()!= UserRoleType.ADMIN){
            throw new UserException(UserErrorCode.NOT_ROLE_ADMIN);
        }
        BannerImage bannerImage = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_IMAGE));


        // S3에서 이미지 삭제
        String imageUrl = bannerImage.getUserImageUrl();
        String s3Key = imageUrl.replace(s3Url, "");
        s3Uploader.deleteS3(s3Key);

        bannerRepository.delete(bannerImage);

        return new BannerImageDeleteRes("삭제 완료");
    }

    public PTImageReadRes readThumbnailPTImage(Long ptId) {
        PTImage ptImage=ptImageRepository.findFirstByPtIdAndType(ptId, ImageType.THUMBNAIL)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_PROFILE));
        return new PTImageReadRes(ptImage.getId(),ptImage.getUserImageUrl());
    }

    public UserImageReadRes getUserProfileImage(Long trainerId) {
        UserImage userImage = userImageRepository.findProfileByUserIdAndType(trainerId, ImageType.PROFILE_IMG)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_PROFILE));
        return new UserImageReadRes(userImage.getId(),userImage.getUserImageUrl());
    }
}