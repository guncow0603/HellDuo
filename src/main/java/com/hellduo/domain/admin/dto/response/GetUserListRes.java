package com.hellduo.domain.admin.dto.response;

public record GetUserListRes(
        Long id,
        String name,
        String email ,          // 이메일
        String gender,        // 성별
        Integer age,           // 나이
        String phoneNumber, // 전화번호
        String nickname,    // 닉네임
        Double weight,    // 체중
        Double height
) {
}
