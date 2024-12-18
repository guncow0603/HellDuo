package com.hellduo.domain.review.dto.request;

public record ReviewCreateReq(
        String title,
        String content,
        Double rating
) {
}
