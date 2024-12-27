package com.hellduo.domain.user.dto.request;

import jakarta.validation.constraints.*;

public record UserProfileUpdateReq (
        @Pattern(regexp = "^[0-9]{10,20}$", message = "전화번호는 10자 이상 20자 이하의 숫자만 포함해야 합니다.")
        String phoneNumber,

        Integer age,  // 나이

        String nickname,  // 닉네임

        Double weight,  // 체중

        Double height  // 키
){
}
