package com.hellduo.domain.pt.dto.response;

import com.hellduo.domain.pt.entity.PTSpecialization;
import com.hellduo.domain.pt.entity.PTStatus;

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
