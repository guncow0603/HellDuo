package com.hellduo.domain.admin.dto.response;

import com.hellduo.domain.user.entity.enums.UserStatus;

public record GetTrainerListRes(
        Long id,
        String email ,          // 이메일
        String name,        // 이름
        String phoneNumber, // 전화번호
        String gender,  // 성별
        Integer age,
        String specialization,  // 전문 분야
        Integer experience,  // 경력(년수)
        String certifications,  // 자격증
        String bio,  // 자기소개
        UserStatus userStatus
) {
}
