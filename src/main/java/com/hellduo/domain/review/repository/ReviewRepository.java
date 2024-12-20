package com.hellduo.domain.review.repository;

import com.hellduo.domain.review.entity.Review;
import com.hellduo.domain.review.exception.ReviewErrorCode;
import com.hellduo.domain.review.exception.ReviewException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTrainerId(Long id);

    List<Review> findAllByTrainerId(Long trainerId);

    default Review findReviewByIdWithThrow(Long id) {
        return findById(id).orElseThrow(()->
                new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));
    };

    Review findByPtId(Long ptId);
}

