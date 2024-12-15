package com.hellduo.domain.imageFile.service;

import com.hellduo.domain.board.entity.Board;
import com.hellduo.domain.board.repository.BoardRepository;
import com.hellduo.domain.imageFile.dto.response.*;
import com.hellduo.domain.imageFile.entity.BoardImage;
import com.hellduo.domain.imageFile.entity.ReviewImage;
import com.hellduo.domain.imageFile.exception.ImageErrorCode;
import com.hellduo.domain.imageFile.exception.ImageException;
import com.hellduo.domain.imageFile.repository.BoardImageRepository;
import com.hellduo.domain.imageFile.repository.ReviewImageRepository;
import com.hellduo.domain.review.entity.Review;
import com.hellduo.domain.review.repository.ReviewRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {

    @Value("${s3.url}")
    private String s3Url;

    private final S3Uploader s3Uploader;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    public UploadReviewImagesRes uploadReviewImages(User user, List<MultipartFile> multipartFiles, Long reviewId) {
        List<String> fileUrlList = uploadFilesToS3(multipartFiles, user.getId(), "reviews/");
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);

        List<ReviewImage> userImageList = createReviewImageList(fileUrlList, user, review);
        reviewImageRepository.saveAll(userImageList);
        return new UploadReviewImagesRes("리뷰 이미지가 업로드 되었습니다.");
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
    private List<ReviewImage> createReviewImageList(List<String> fileUrls, User user, Review review) {
        List<ReviewImage> reviewImageList = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            reviewImageList.add(ReviewImage.builder()
                    .user(user)
                    .reviewImageUrl(s3Url+fileUrl)
                    .review(review)
                    .build());
        }
        return reviewImageList;
    }

    public List<GetReviewImagesRes> getReviewImages(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId);

        if (reviewImages.isEmpty()) {
            throw new ImageException(ImageErrorCode.NOT_FOUND_IMAGE);
        }

        List<GetReviewImagesRes> response = new ArrayList<>();

        for (ReviewImage reviewImage : reviewImages) {
            response.add(new GetReviewImagesRes(reviewImage.getId(),reviewImage.getReviewImageUrl()));
        }

        return response;
    }

    public GetReviewImageRes getReviewImage(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId);

        ReviewImage reviewImage = reviewImages.get(0);
        return new GetReviewImageRes(
                reviewImage.getId(),
                reviewImage.getReviewImageUrl());
    }
}

