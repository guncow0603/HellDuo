package com.hellduo.domain.imageFile.service;

import com.hellduo.domain.imageFile.dto.response.GetImagesRes;
import com.hellduo.domain.imageFile.dto.response.GetThumbnailRes;
import com.hellduo.domain.imageFile.dto.response.ImageDeleteRes;
import com.hellduo.domain.imageFile.dto.response.UploadImagesRes;
import com.hellduo.domain.imageFile.entity.ImageFile;
import com.hellduo.domain.imageFile.entity.enums.ImageType;
import com.hellduo.domain.imageFile.exception.ImageErrorCode;
import com.hellduo.domain.imageFile.exception.ImageException;
import com.hellduo.domain.imageFile.repository.ImageFileRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final ImageFileRepository imageFileRepository;
    private final S3Uploader s3Uploader;

    @Value("${S3_URL}")
    private String s3Url;

    public UploadImagesRes uploadImages(String category, Long targetId, User user, List<MultipartFile> multipartFiles) {
        Long userId = user.getId();

        if (category.equals("profile")) {
            deleteImages(targetId , category, user);
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "profile/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.PROFILE_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.save(imageFileList.get(0));

            return new UploadImagesRes("프로필 이미지가 수정되었습니다.");

        }else if(category.equals("certification")){
            if(!user.getRole().equals(UserRoleType.TRAINER)){
                throw  new UserException(UserErrorCode.NOT_ROLE_TRAINER);
            }
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "certification/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.CERTS_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.saveAll(imageFileList);

            return new UploadImagesRes("자격증 이미지가 업로드되었습니다.");

        }else if(category.equals("pt")){
            if(!user.getRole().equals(UserRoleType.TRAINER)){
                throw  new UserException(UserErrorCode.NOT_ROLE_TRAINER);
            }
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "pt/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.PT_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.saveAll(imageFileList);

            return new UploadImagesRes("피티 이미지가 업로드되었습니다.");

        }else if(category.equals("banner")){
            if(!user.getRole().equals(UserRoleType.ADMIN)){
                throw  new UserException(UserErrorCode.NOT_ROLE_ADMIN);
            }
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "banner/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.BANNER_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.saveAll(imageFileList);

            return new UploadImagesRes("배너 이미지가 업로드되었습니다.");

        }else if(category.equals("board")){
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "board/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.BOARD_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.saveAll(imageFileList);

            return new UploadImagesRes("게시판 이미지가 업로드되었습니다.");

        }else if(category.equals("review")) {
            if(!user.getRole().equals(UserRoleType.USER)){
                throw  new UserException(UserErrorCode.NOT_ROLE_USER);
            }
            // S3에 파일 업로드
            List<String> fileUrlList = uploadFilesToS3(multipartFiles, userId, "review/");

            // 업로드된 파일의 정보로 ImageFile 엔티티 생성
            List<ImageFile> imageFileList = createImageFileList(fileUrlList , targetId, ImageType.REVIEW_IMG,user);

            // 첫 번째 이미지를 저장 (프로필 이미지 하나만 저장)
            imageFileRepository.saveAll(imageFileList);

            return new UploadImagesRes("리뷰 이미지가 업로드되었습니다.");
        } else {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }

    }


    public List<GetImagesRes> getImages(String category, Long targetId) {
        //  category를 ImageType으로 변환
        ImageType imageType = convertCategoryToImageType(category);

        List<ImageFile> imageFiles;

        if(imageType == ImageType.BANNER_IMG) {
            imageFiles = imageFileRepository.findByType(imageType);
        }else {
            //  ImageFile 조회
            imageFiles = imageFileRepository.findByTypeAndTargetId(imageType, targetId);
        }

        //  DTO로 변환 =
        List<GetImagesRes> getImagesRes = new ArrayList<>();
        for (ImageFile imageFile : imageFiles) {
            getImagesRes.add(new GetImagesRes(imageFile.getImageUrl(), imageFile.getId()));
        }
        return getImagesRes;
    }


    public GetThumbnailRes getThumbnailImage(String category, Long targetId) {
        // 1. category를 ImageType으로 변환
        ImageType imageType = convertCategoryToImageType(category);

        // 2. 썸네일 이미지 조회
        // 썸네일 이미지는 특정 type에 맞는 이미지를 선택합니다.
        ImageFile thumbnailImageFile = imageFileRepository.findTopByTypeAndTargetId(imageType, targetId);

        // 3. 이미지가 존재하는지 확인
        if (thumbnailImageFile != null) {
            // 4. 썸네일 URL 반환
            return new GetThumbnailRes(thumbnailImageFile.getImageUrl());
        } else {
            // 썸네일 이미지가 없다면, 기본 이미지를 반환하거나 null을 반환할 수 있습니다.
            return new GetThumbnailRes(null); // 기본 URL
        }
    }

    public ImageDeleteRes deleteImage(Long imageId, User user) {
        // 이미지 조회
        ImageFile imageFile = imageFileRepository.findById(imageId)
                .orElseThrow(() -> new ImageException(ImageErrorCode.NOT_FOUND_IMAGE));

        if(user.getId().equals(imageFile.getUser().getId())){
            throw new ImageException(ImageErrorCode.IMAGE_CURRENT_USER);
        }

        String imageUrl = imageFile.getImageUrl();
        String s3Key = imageUrl.replace(s3Url, "");
        s3Uploader.deleteS3(s3Key);

        // 이미지 삭제
        imageFileRepository.delete(imageFile);

        // 성공 응답 반환
        return new ImageDeleteRes("이미지 삭제 완료");
    }

    public void deleteImages(Long targetId, String category,User user) {
        // category를 ImageType으로 변환
        ImageType imageType = convertCategoryToImageType(category);

        // targetId와 category에 해당하는 이미지 조회
        List<ImageFile> imageFiles = imageFileRepository.findByTargetIdAndType(targetId, imageType);

        if(user.getId().equals(imageFiles.get(0).getUser().getId())){
            throw new ImageException(ImageErrorCode.IMAGE_CURRENT_USER);
        }

        // 조회된 이미지가 없으면 예외 처리
        if (imageFiles.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }

        for (ImageFile imageFile : imageFiles) {
            String imageUrl = imageFile.getImageUrl();
            String s3Key = imageUrl.replace(s3Url, "");
            s3Uploader.deleteS3(s3Key);
        }

        // 이미지 삭제
        imageFileRepository.deleteAll(imageFiles);
    }

    private ImageType convertCategoryToImageType(String category) {
        // category 문자열을 ImageType Enum으로 매핑
        return switch (category.toLowerCase()) {
            case "profile" -> ImageType.PROFILE_IMG;
            case "certification" -> ImageType.CERTS_IMG;
            case "pt" -> ImageType.PT_IMG;
            case "banner" -> ImageType.BANNER_IMG;
            case "review" -> ImageType.REVIEW_IMG;
            case "board" -> ImageType.BOARD_IMG;
            default -> throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        };
    }


    private List<ImageFile> createImageFileList(List<String> fileUrls,Long targetId , ImageType imageType,User user) {
        List<ImageFile> imageFileList = new ArrayList<>();

        for (String fileUrl : fileUrls) {
            ImageFile imageFile = ImageFile.builder()
                    .targetId(targetId)
                    .imageUrl(s3Url+fileUrl)
                    .type(imageType)
                    .user(user)
                    .build();
            imageFileList.add(imageFile);
        }

        return imageFileList;
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
}