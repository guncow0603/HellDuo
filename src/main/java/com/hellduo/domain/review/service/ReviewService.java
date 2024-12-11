package com.hellduo.domain.review.service;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.PTStatus;
import com.hellduo.domain.pt.exception.PTErrorCode;
import com.hellduo.domain.pt.exception.PTException;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.review.dto.request.ReviewCreateReq;
import com.hellduo.domain.review.dto.response.ReviewCreateRes;
import com.hellduo.domain.review.entity.Review;
import com.hellduo.domain.review.repository.ReviewRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.exception.UserErrorCode;
import com.hellduo.domain.user.exception.UserException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final PTRepository ptRepository;

    public ReviewCreateRes reviewCreate(@Valid ReviewCreateReq req, User user) {
        PT pt = ptRepository.findPTByIdWithThrow(req.ptId());

        if (pt.getStatus() != PTStatus.COMPLETED) {
            throw new PTException(PTErrorCode.PT_NOT_COMPLETED);
        }

        if(user.getRole() != UserRoleType.USER) {
            throw new UserException(UserErrorCode.NOT_ROLE_USER);
        }

        User trainer = pt.getTrainer();

        Review review = Review.builder().
                title(req.title()).
                content(req.content()).
                pt(pt).
                trainer(trainer).
                rating(req.rating()).
                build();
        reviewRepository.save(review);

        updateTrainerRating(trainer);

        return new ReviewCreateRes("후기 작성 완료.");
    }

    // 트레이너의 평균 평점 계산 메서드
    private void updateTrainerRating(User trainer) {
        List<Review> reviews = reviewRepository.findByTrainerId(trainer.getId());  // 트레이너가 받은 모든 리뷰 조회

        if (reviews.isEmpty()) {
            trainer.updateRating(0.0);  // 리뷰가 없으면 평점은 0
        } else {
            double sum = 0;
            for (Review review : reviews) {
                sum += review.getRating();
            }
            double average = sum / reviews.size();  // 평균 평점 계산
            trainer.updateRating(average);  // 트레이너의 평균 평점 업데이트
        }
    }

}