package com.hellduo.domain.user.dto.response;

public record BestRatingTrainerRes(
        Long trainerId,
        String name,
        Double rating,
        String specialization
) {
}
