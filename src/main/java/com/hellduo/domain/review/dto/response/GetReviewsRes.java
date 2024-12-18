package com.hellduo.domain.review.dto.response;

public record GetReviewsRes (
        Long reviewId,
        String title,
        Long ptId,
        Long trainerId,
        Double rating
){
}
