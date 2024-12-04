package com.hellduo.domain.pt.dto.response;

import java.time.LocalDateTime;

public record PTReadRes(
        Long ptId,                 // PT 세션 ID
        Long trainerId,
        String title,
        LocalDateTime scheduledDate, // 예약 날짜 및 시간
        Long price,               // PT 비용
        String description,      // 세션 설명
        String trainerName,      // 트레이너 이름
        String specialization,
        String userName,         // 예약자 이름
        String status            // PT 상태
) {
}
