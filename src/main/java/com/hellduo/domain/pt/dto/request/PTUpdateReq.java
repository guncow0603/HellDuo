package com.hellduo.domain.pt.dto.request;

import com.hellduo.domain.pt.entity.PTSpecialization;

import java.time.LocalDateTime;

public record PTUpdateReq(
        String title,
        PTSpecialization specialization,
        LocalDateTime scheduledDate,
        Long price,
        String description
) {
}
