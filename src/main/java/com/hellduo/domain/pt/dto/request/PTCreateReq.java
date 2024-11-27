package com.hellduo.domain.pt.dto.request;

import com.hellduo.domain.pt.entity.PTSpecialization;

import java.time.LocalDateTime;

public record PTCreateReq(
        String title,
        PTSpecialization specialization,
        LocalDateTime scheduledDate,
        Integer price,
        String description
) { }
