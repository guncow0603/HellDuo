package com.hellduo.domain.review.dto.request;

public record ReviewCreateReq(
        Long ptId,
        String title,
        String content,
        Double rating
) {
}
