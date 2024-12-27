package com.hellduo.domain.user.dto.request;

import com.hellduo.domain.user.entity.enums.Specialization;
import jakarta.validation.constraints.*;

public record TrainerProfileUpdateReq(
        @Pattern(regexp = "^[0-9]{10,20}$", message = "전화번호는 10자 이상 20자 이하의 숫자만 포함해야 합니다.")
        String phoneNumber,  // 전화번호

        Specialization specialization,  // 전문 분야

        @Min(value = 0, message = "경력은 0 이상이어야 합니다.")
        Integer experience,  // 경력(년수)

        String certifications,  // 자격증

        @Size(max = 500, message = "자기소개는 500자 이하로 입력해주세요.")
        String bio  // 자기소개
) {
}
