package com.hellduo.domain.pt.dto.response;

import com.hellduo.domain.pt.entity.PTSpecialization;
import com.hellduo.domain.pt.entity.PTStatus;

import java.time.LocalDateTime;

public record PTsReadRes (
        Long ptId,
        String title,
        PTSpecialization specialization,
        LocalDateTime scheduledDate,
        int price,
        PTStatus ptStatus

){
}
