package com.hellduo.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginReq(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,  // 이메일

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern( regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#!@$ %^])[a-zA-Z0-9@#$%^!]*$",
                message = "영어, 숫자, 특수문자(!,@,#,$,%,^) 조합으로 입력해주세요.")
        @Size(min = 8, max = 25, message = "최소 8자, 최대 25자로 입력해주세요.")
        String password// 비밀번호

) {
}
