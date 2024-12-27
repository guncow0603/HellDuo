package com.hellduo.domain.user.dto.request;

import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import jakarta.validation.constraints.*;

public record TrainerSignupReq (
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 50, message = "이메일은 최대 50자까지 입력 가능합니다.")
        String email,  // 이메일

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#!@$%^])[a-zA-Z0-9@#$%^!]*$",
                message = "영어, 숫자, 특수문자(!,@,#,$,%,^) 조합으로 입력해주세요.")
        @Size(min = 8, max = 25, message = "비밀번호는 최소 8자, 최대 25자여야 합니다.")
        String password,  // 비밀번호

        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        @Size(min = 8, max = 25, message = "비밀번호 확인은 최소 8자, 최대 25자여야 합니다.")
        String passwordConfirm,  // 비밀번호 확인

        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(max = 50, message = "이름은 최대 50자까지 입력 가능합니다.")
        String name,  // 이름

        @NotNull(message = "성별은 필수 입력값입니다.")
        Gender gender,  // 성별

        @NotNull(message = "연령은 필수 입력값입니다.")
        @Min(value = 18, message = "연령은 최소 18세 이상이어야 합니다.")
        @Max(value = 100, message = "연령은 최대 100세 이하이어야 합니다.")
        Integer age,  // 나이

        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(regexp = "^[0-9]{10,20}$", message = "전화번호는 10자 이상 20자 이하의 숫자만 포함해야 합니다.")
        String phoneNumber,  // 전화번호

        @NotNull(message = "전문 분야는 필수 입력값입니다.")
        Specialization specialization,  // 전문 분야

        @NotNull(message = "경력은 필수 입력값입니다.")
        @Min(value = 0, message = "경력은 0 이상이어야 합니다.")
        Integer experience,  // 경력(년수)

        @NotBlank(message = "자격증 정보는 필수 입력값입니다.")
        @Size(max = 200, message = "자격증 정보는 최대 200자까지 입력 가능합니다.")
        String certifications,  // 자격증

        @NotBlank(message = "자기소개는 필수 입력값입니다.")
        @Size(max = 500, message = "자기소개는 최대 500자까지 입력 가능합니다.")
        String bio  // 자기소개
){
}