package com.hellduo.domain.user.dto.request;

import jakarta.validation.constraints.*;

public record UserProfileUpdateReq (
        String phoneNumber,

        Integer age,  // 나이

        String nickname,  // 닉네임

        Double weight,  // 체중

        Double height  // 키
){
}
