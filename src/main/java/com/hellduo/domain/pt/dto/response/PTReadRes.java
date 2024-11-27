package com.hellduo.domain.pt.dto.response;

import java.time.LocalDateTime;

public record PTReadRes(
        Long ptId,                 // PT 세션 ID
        LocalDateTime scheduledDate, // 예약 날짜 및 시간
        int price,               // PT 비용
        String description,      // 세션 설명
        String trainerName,      // 트레이너 이름
        String userName,         // 예약자 이름
        String status            // PT 상태
) {
}
