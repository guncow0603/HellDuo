package com.hellduo.domain.review.dto.request;

import jakarta.validation.constraints.*;

public record ReviewCreateReq(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
        String content,

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 0, message = "평점은 0 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        Double rating
) {
}
