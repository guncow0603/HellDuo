package com.hellduo.domain.review.dto.response;

public record GetReviewsRes (
        String title,
        String content,
        Long ptId,
        Long trainerId,
        Double rating
){
}
