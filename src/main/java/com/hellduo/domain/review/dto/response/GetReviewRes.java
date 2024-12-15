package com.hellduo.domain.review.dto.response;

public record GetReviewRes(
        Long reviewId,
        String title,
        String content,
        Double rating
) {
}
