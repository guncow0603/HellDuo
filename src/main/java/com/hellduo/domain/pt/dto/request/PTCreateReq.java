package com.hellduo.domain.pt.dto.request;

import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PTCreateReq(

        @NotBlank(message = "제목은 필수입니다.") // 제목은 필수
        @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.") // 제목 최대 100자
        String title,

        @NotBlank(message = "내용은 필수입니다.") // 내용은 필수
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        String description,

        @NotBlank(message = "예약 시간은 필수입니다.") // 예약 시간은 필수
        LocalDateTime scheduledDate,

        @NotBlank(message = "가격은 필수입니다.") // 가격은 필수
        Long price,

        @NotBlank(message = "전문 분야는 필수입니다.") // 전문 분야는 필수
        PTSpecialization specialization,

        @NotBlank(message = "위도는 필수입니다.") // 위도는 필수
        Double latitude,

        @NotBlank(message = "경도는 필수입니다.") // 경도는 필수
        Double longitude,

        @NotBlank(message = "주소는 필수입니다.") // 주소는 필수
        String address
) { }