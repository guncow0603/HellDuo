package com.hellduo.domain.pt.dto.response;

import java.time.LocalDateTime;

public record PTsReadRes (
        Long ptId,
        String title,
        String specialization,
        LocalDateTime scheduledDate,
        Long price,
        String ptStatus

){
}
