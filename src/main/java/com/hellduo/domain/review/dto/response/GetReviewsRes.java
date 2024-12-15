package com.hellduo.domain.review.dto.response;

public record GetReviewsRes (
        Long reviewId,
        String title,
        String content,
        Long ptId,
        Long trainerId,
        Double rating
){
}
