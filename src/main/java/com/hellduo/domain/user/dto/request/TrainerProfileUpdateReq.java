package com.hellduo.domain.user.dto.request;

import com.hellduo.domain.user.entity.enums.Specialization;

public record TrainerProfileUpdateReq(
        String phoneNumber,  // 전화번호
        Specialization specialization,  // 전문 분야
        Integer experience,  // 경력(년수)
        String certifications,  // 자격증
        String bio  // 자기소개
) {
}
