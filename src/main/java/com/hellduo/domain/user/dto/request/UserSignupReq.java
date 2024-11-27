package com.hellduo.domain.user.dto.request;

import com.hellduo.domain.user.entity.enums.Gender;
import jakarta.validation.constraints.*;

public record UserSignupReq(

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,  // 이메일

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern( regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#!@$ %^])[a-zA-Z0-9@#$%^!]*$",
                  message = "영어, 숫자, 특수문자(!,@,#,$,%,^) 조합으로 입력해주세요.")
        @Size(min = 8, max = 25, message = "최소 8자, 최대 20자로 입력해주세요.")
        String password,  // 비밀번호

        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        String passwordConfirm,  // 비밀번호 확인

        @NotNull(message = "성별은 필수 입력값입니다.")
        Gender gender,  // 성별

        @NotNull(message = "연령은 필수 입력값입니다.")
        Integer age,  // 나이

        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        String phoneNumber,  // 전화번호

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        String nickname,  // 닉네임

        @NotNull(message = "체중은 필수 입력값입니다.")
        Double weight,  // 체중

        @NotNull(message = "키는 필수 입력값입니다.")
        Double height,  // 키

        boolean admin,
        String adminToken
    ){
}
